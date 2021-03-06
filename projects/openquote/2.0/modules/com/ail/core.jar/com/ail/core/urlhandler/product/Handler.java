/* Copyright Applied Industrial Logic Limited 2008. All rights reserved. */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.ail.core.urlhandler.product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.HttpURLConnection;

import com.ail.core.CoreProxy;
import com.ail.core.ThreadLocale;

/**
 * The handler deals with URLs of the form: "product://". All this class does is
 * to transparently transform a "product://" URL into a reference to a resource
 * in the product repository. The Handler is sensitive to the convention used in
 * the repository to store locale specific content. Specifically, the language
 * code is appended to the content path. If the path ends in a file extension,
 * the language is appended immediately before the extension. If locale specific
 * content cannot be found, then the default content (without any language
 * specified) is returned. </p> For example, for an German language locale the
 * URL "http://localhost/file.html" would initially be searched for as
 * "http://localhost/file_de.html". If this is not found, then
 * "http://localhost/file.html" would be resolved instead.
 * <p/>
 * The details of the location of the content repository and the credentials to
 * use to access it are picked up from parameters in the configure systemm's
 * "ProductURLHandler" property group. Three properties are referenced in this
 * group: "BaseURL" defines the location of the repository, the "Username" and
 * "Password" parameters define the credentials to use to connect to the repo.
 * For example, a URL like:
 * <code>product://localhost:8080/Demo/Demo/Welcome.html</code> is translated
 * into a call to <code>&lt;BaseURL&gt;/Demo/Demo/Welcome.html</code>.
 * <p/>
 */
public class Handler extends URLStreamHandler {

    protected URLConnection openConnection(URL productURL) throws IOException {
        URLConnection urlConnection = null;
        CoreProxy cp = new CoreProxy();

        String username = cp.getParameterValue("ProductURLHandler.Username");
        String password = cp.getParameterValue("ProductURLHandler.Password");
        String protocol = cp.getParameterValue("ProductURLHandler.Protocol");
        String realm = cp.getParameterValue("ProductURLHandler.Realm");
        String host = cp.getParameterValue("ProductURLHandler.Host");
        Integer port = new Integer(cp.getParameterValue("ProductURLHandler.Port"));
        String path = cp.getParameterValue("ProductURLHandler.Path");

        String baseURL = new URL(protocol, host, port, path).toExternalForm();

        String language = ThreadLocale.getThreadLocale().getLanguage();

        HttpClient httpClient = createHttpClient(host, port, realm, username, password);

        // 1) First try to fetch the content with the thread's locale.
        // 2) If that yields a FileNotFound, try to fetch it without
        // the locale.
        // 3) If that fails with a FileNotFound, throw a FileNotFound
        // 4) Any other exceptions are passed on as they are.
        try {
            urlConnection = connectUsingLanguage(productURL, baseURL, httpClient, language);
        } catch (IOException e1) {
            if (e1 instanceof FileNotFoundException) {
                try {
                    urlConnection = connectUsingLanguage(productURL, baseURL, httpClient, "");
                } catch (IOException e2) {
                    if (e1 instanceof FileNotFoundException) {
                        throw new FileNotFoundException(productURL.toString());
                    }
                    throw e2;
                }
            } else {
                throw e1;
            }
        }

        return urlConnection;
    }

    /**
     * Attempt to connect to a specified piece of content using specified
     * authentication and language. The actual URL is built from the URL,
     * baseURL and language passed in.
     * 
     * @param productURL
     * @param baseURL
     * @param authToken
     * @param language
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    URLConnection connectUsingLanguage(URL productURL, String baseURL, HttpClient httpClient, String language) throws MalformedURLException, IOException {
        String canonicalPath = baseURL + productURL.getPath();

        URL actualURL = new URL(addLanguageToURL(canonicalPath, language));

        GetMethod httpGetMethod = new GetMethod(actualURL.toExternalForm());

        int status = httpClient.executeMethod(httpGetMethod);

        if (status==404 || status==412) {
            httpGetMethod.releaseConnection();
            throw new FileNotFoundException();
        }
        
        try {
            return createProxy(new HttpURLConnection(httpGetMethod, actualURL));
        } catch (Exception e) {
            throw new IOException("Failed to create proxy to read '" + actualURL + "', encountered:" + e.toString(), e);
        }
    }

    /**
     * Create a dynamic proxy around the URLConnection to the content. We need
     * to do this because in order for us, here in the handler, to detect if
     * content really exists (which we have to do in order to pick up the right
     * localisation) we have to open the connection. However, some 3rd party
     * libs (e.g. Apache FOP) assume that the connection has not been opened and
     * thus they are safe to call methods like setAllowUserInteraction() on the
     * connection. However, if the connection is already open calling these
     * methods will result in an IllegalStateExeption. So, we use a dynamic
     * proxy here to mask out those methods calls.
     * 
     * @param connection
     * @return A Proxy URL connection.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     */
    private URLConnection createProxy(final URLConnection connection) throws IOException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(URLConnection.class);

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object obj, Method method, Method proceed, Object[] args) throws Throwable {
                if ("setAllowUserInteraction".equals(method.getName()) || "setDoInput".equals(method.getName()) || "connect".equals(method.getName())) {
                    return null;
                } else {
                    return method.invoke(connection, args);
                }
            }
        };

        return (URLConnection) factory.create(new Class<?>[] { URL.class }, new Object[] { null }, handler);
    }

    /**
     * Add a language code to a content path. The convention is to include the
     * language (ISO code) before the last '.' in the URL. For example, the URL
     * is "http://localhost:8080/folder/file.html", would become
     * "http://localhost:8080/folder/file_en.html". Where the filename does not
     * contain a '.', the language is simply appended. So
     * "http://localhot:8080/folder/file" would become
     * "http://8080/folder/file_en".
     * 
     * @param path
     * @param language
     * @return canonical path modified to include the language
     */
    String addLanguageToURL(final String path, final String language) {
        // if we're not sent a language, return the path as it is.
        if ("".equals(language) || language == null) {
            return path;
        }

        String modifiedPath;

        // if the path ends with a file extension (e.g. .html, .txt, .whatever),
        // then insert _<language> just before the '.'. Otherwise, leave the
        // path as it is.
        modifiedPath = path.replaceAll("(\\.[^/.]*$)", "_" + language + "$1");

        if (path.equals(modifiedPath)) {
            modifiedPath = path + "_" + language;
        }

        return modifiedPath;
    }

    /**
     * Create an instance of HttpClient matching the parameters passed.
     */
    private HttpClient createHttpClient(String host, int port, String realm, String username, String password) {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(new AuthScope(host, port, realm), new UsernamePasswordCredentials(username, password));

        List<String> authPrefs = new ArrayList<String>();
        authPrefs.add(AuthPolicy.BASIC);
        authPrefs.add(AuthPolicy.NTLM);
        authPrefs.add(AuthPolicy.DIGEST);
        
        client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);

        return client;
    }
}
