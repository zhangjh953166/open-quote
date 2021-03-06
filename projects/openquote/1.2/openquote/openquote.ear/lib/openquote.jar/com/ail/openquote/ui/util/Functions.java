/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.openquote.ui.util;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Attribute;
import com.ail.core.Type;
import com.ail.openquote.ui.PageElement;

/**
 * This class defines a collection of functions used by the classes in {@link com.ail.openquote.ui}.
 */
public class Functions {
    private static SimpleDateFormat longFormat=new SimpleDateFormat("d MMMMM, yyyy");

    /** 
     * Convert an XPath expression in to a format that will be accepted as an HTML element's id.
     * The data binding mechanism used in openquote's UI is based on xpath. A field in a UI form
     * is bound to the quote object by means of the field's 'id'; as the pages are generated the
     * IDs are give the value of an xpath expression pointing into the quote model.<p>
     * However, xpath expressions may contain characters that aren't compatible with HTML IDs (one 
     * example being the single quote character). This method converts xpaths into a form that is
     * safe to be used as IDs, and is also able to be converted back into a xpath by the {@link #idToXpath(String)}
     * method. 
     * @param XPath expression
     * @return HTML Id
     */
    public static String xpathToId(String xpath) {
        return xpath.replace('\'', '#');
    }

    /**
     * @see #xpathToId(String)
     * @param HTML Id
     * @return XPath expression
     */
    public static String idToXpath(String id) {
        return id.replace('#', '\'');
    }

    /**
     * Determine if a String is empty - null or zero length
     * @param s String to check
     * @return true if 's' is empty, false otherwise.
     */
    public static boolean isEmpty(String s) {
        return (s==null || s.length()==0);
    }
    
    /**
     * Convert null strings into empty strings. When a UI component is rendering it'll frequently
     * want to render null strings. The default java behavior when you ask to output a null String
     * is to write "null" to the output - which isn't what we typically want on the UI. 
     * @param s String to check
     * @return "" if the string was null, or the value of the string if it was not.
     */
    public static String hideNull(String s) {
        return (s==null) ? "" : s;
    }

    @SuppressWarnings("unchecked")
    public static Properties getOperationParameters(ActionRequest request) {
        String nm;
        String[] parts;
        Properties params=new Properties();

        for(Enumeration<String> en=request.getParameterNames() ; en.hasMoreElements() ;) {
            nm=en.nextElement();
            if (nm.startsWith("op=")) {
                for(String param: nm.split(":")) {
                    parts=param.split("=");
                    if (parts.length==2) {
                        params.put(parts[0], parts[1]);
                    }
                }
                break;
            }
            else if ("op".equals(nm)) {
                //TODO handle things like 'op=Save:arg=immediate'
                params.put("op", request.getParameter("op"));
                break;
            }
        }
        
        return params;
    }

    /**
     * Return the name of the portal page that a render response relates to.
     * From a PageElement we don't have much information to go on if we want to
     * query the environment that the portlet we're associated with is running in.
     * In the case of the LoginSection, we need to know which portal page we're
     * deployed to in order to make the jump from the public portal to the  
     * authenticated one.
     * The action URL for non-authenticated takes this kind of form:
     *    /portal/portal/<portal-name>/<page-name>/<window-name>
     * When authenticated the same URL looks like this:
     *    /portal/auth/portal/<portal-name>/<page-name>/<window-name>
     */
    public static String getPortalPageName(RenderResponse response) {
        String[] actionUrlPart=response.createActionURL().toString().split("/");
        
        if ("auth".equals(actionUrlPart[2])) {
            return actionUrlPart[5];
        }
        else {
            return actionUrlPart[4];
        }
    }

    /**
     * Return the name of the portal that a render response relates to.
     * The action URL for non-authenticated takes this kind of form:
     *    /portal/portal/<portal-name>/<page-name>/<window-name>
     * When authenticated the same URL looks like this:
     *    /portal/auth/portal/<portal-name>/<page-name>/<window-name>
     */
    public static String getPortalName(RenderResponse response) {
        String[] actionUrlPart=response.createActionURL().toString().split("/");
        
        if ("auth".equals(actionUrlPart[2])) {
            return actionUrlPart[4];
        }
        else {
            return actionUrlPart[3];
        }
    }


    
    /**
     * Return true if the specified object has any UI error markers associated with it. UI 
     * components use the conversion of attaching error attributes to model element to indicate
     * validation failures. This method will return true if it finds any such attributes
     * associated with the specified object.
     * @param model Object to check for error markers
     * @return true if error markers are found, false otherwise (including if model==null).
     */
    public static boolean hasErrorMarkers(Type model) {
    	if (model!=null) {
	        for(Attribute a: model.getAttribute()) {
	            if(a.getId().startsWith("error.")) {
	                return true;
	            }
	        }
    	}

        return false;
    }

    /**
     * Add a new error to a model.
     * @param id unique error ID
     * @param message Message to be displayed
     * @param model Model to attach the error to
     */
    public static void addError(String id, String message, Type model) {
    	model.addAttribute(new Attribute("error."+id, message, "string"));
    }
    
    /**
     * return true if the specified object has a specific error marker associated with it.
     * @see #hasErrorMarkers(Type)
     * @param id Name of error marker to look for
     * @param model Object to check for the error marker
     * @return true if the error marker is found, false otherwise (including if model==null).
     */
    public static boolean hasErrorMarker(String id, Type model) {
    	if (model!=null) {
	        for(Attribute a: model.getAttribute()) {
	            if(a.getId().startsWith("error."+id)) {
	                return true;
	            }
	        }
    	}

        return false;
    }

    private static void lookupErrorTranslation(String error, StringBuffer errors, List<ErrorText> errorList) {
    	boolean errorFound=false;
    	
    	if (errorList.size()!=0) {
    		for(ErrorText e: errorList) {
    			if (error.equals(e.getError())) {
    		    	if (errors.length()!=0) {
    		    		errors.append(", ");
    		    	}
    		    	errors.append(e.getText());
    		    	errorFound=true;
    			}
    		}
    	}

    	if (!errorFound) {
    		errors.append(error);
    	}
    }
    
    /**
     * Find the error(s) (if any) associated with an element in a model, and return them.
     * @param errorFilter Which errors to return
     * @param model The model to look in for the error
     * @return The error message, or "&nbsp;" (an empty String) if no message is found.
     */
    public static String findError(String errorFilter, Type model, PageElement element) {
    	StringBuffer error=new StringBuffer();

    	for(Attribute attr: model.getAttribute()) {
    		if (attr.getId().startsWith("error."+errorFilter)) {
    			lookupErrorTranslation(attr.getValue(), error, element.getErrorText());
    		}
    	}
    	
    	return (error.length()==0) ? "&nbsp;" : error.toString();
    }
    
    /**
     * Find all the the errors (if any) associated with an element in a model, and return them.
     * @param model The model to look in for the error
     * @return The error message, or "&nbsp;" (an empty String) if no message is found.
     */
    public static String findErrors(Type model, PageElement element) {
    	return findError("", model, element);
    }
    
    /**
     * Remove error marker attributes attached to the specified object. The UI components use
     * the conversion of attaching error attributes to model element to indicate validation 
     * failures. This method strips any such markers from the object passed in. Note: It doesn't
     * attempt to walk the object tree, it will only remove markers from the object itself.
     * @param model Object to remove markers from.
     */
    public static void removeErrorMarkers(Type model) {
        ArrayList<Attribute> toDelete=new ArrayList<Attribute>();
        
        // Delete all the error attributes from the proposer. To avoid a ConcurrentModificationException
        // this is done in two stags: 1) add the error attributes to the 'toDelete' ArrayList; 2) delete
        // all the attributes in the toDelete list from the proposer.
        for(Attribute a: model.getAttribute()) {
            if(a.getId().startsWith("error.")) {
                toDelete.add(a);
            }
        }
        
        for(Attribute a: toDelete) {
            model.removeAttribute(a);
        }
    }
    
    /**
     * Return a string representation of a date in "long" format. Long format is: "d MMMMM, yyyy". For example:
     * 10 November, 2007 
     * @param date
     * @return String representation of <i>date</i>
     */
    public static String longDate(Date date) {
        synchronized(longFormat) {
            return longFormat.format(date);
        }
    }

    /**
     * Product URLs (using the "product" protocol) are only accessible within the OpenQuote
     * server, by virtue of {@link com.ail.code.urlhandler.product.Handler Handler}. This 
     * method converts a product URL into a form which can be used externally.
     * @param url URL to be converted
     * @return External URL
     * @throws MalformedURLException 
     */
    public static URL convertProductUrlToExternalForm(URL productUrl) throws MalformedURLException {
    	return new URL("http", productUrl.getHost(), productUrl.getPort(), "/alfresco/download/direct?path=/Company%20Home/Product"+productUrl.getPath()); 
    }
    
    /**
     * Products frequently refer to content from their Registries or Pageflows by "relative" URLs. This method
     * expands relative URLs into absolute product URLs - i.e. a URL using the "product:" protocol. A relative URL 
     * is one that starts with "~/", where "~" is shorthand for the product's home location. None relative URLs are
     * returned without modification.
     * @param url URL to be checked and expanded if necessary
     * @param request Associated request
     * @param productTypeId Product to be used in the expanded URL
     * @return Expanded URL if it was relative, URL as passed in otherwise.
     */
    public static String expandRelativeUrlToProductUrl(String url, RenderRequest request, String productTypeId) {
        if (url.startsWith("~/")) {
            return "product://"+request.getServerName()+":"+request.getServerPort()+"/"+productTypeId.replace('.', '/')+url.substring(1); 
        }
        else {
            return url;
        }
    }
    
    /**
     * Convert a list of Strings into a semicolon separated list.
     * @param list List to be converted
     * @return semicolon separated list of values from the list.
     */
    public static String convertListToCsv(List<String> list) {
    	StringBuffer ret=new StringBuffer();
    	
    	for(Iterator<String> e=list.iterator() ; e.hasNext() ; ) {
    		ret.append(e.next());
    		if (e.hasNext()) {
    			ret.append(";");
    		}
    	}
    	
    	return ret.toString();
    }

    /**
     * Convert a String of values in semicolon separated format into a List<String>.
     * @param csv String to be converted
     * @return List of strings
     */
    public static List<String> convertCsvToList(String csv) {
    	return new ArrayList<String>(Arrays.asList(csv.split("[ \t]*+;[ \t]*+")));
    }
}
