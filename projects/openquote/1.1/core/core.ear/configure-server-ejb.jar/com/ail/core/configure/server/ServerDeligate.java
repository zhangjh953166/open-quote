/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

package com.ail.core.configure.server;

import java.security.Principal;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.ail.core.Version;
import com.ail.core.configure.finder.GetClassListArg;

/**
 * This class acts as a business delegate to the ConfigureServer EJB.
 * @version $Revision: 1.7 $
 * @state $State: Exp $
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/configure-server-ejb.jar/com/ail/core/configure/server/ServerDeligate.java,v $
 */
public class ServerDeligate {
    private Principal user=null;
    private Server server=null;
    private String username=null;
    private String password=null;

    public ServerDeligate(Principal principal) {
        this.user=principal;
    }
    
    public ServerDeligate(String username, String password) {
        this.username=username;
        this.password=password;
    }
    
    private Server configureServer() throws Exception {
        if (server==null) {
            Context context;
            
            // If we're running outside of a J2EE container, we expect to have been given
            // a username & password to get access to the server. If we're already in the
            // appserver, we simply rely on the container for this stuff.
            if (username!=null && password!=null) {
                Properties props=new Properties();
                props.put( Context.SECURITY_PRINCIPAL, username );
                props.put( Context.SECURITY_CREDENTIALS, password ); 
                context=new InitialContext(props);
            }
            else {
                context=new InitialContext();
            }
            
            ServerHome home=(ServerHome)context.lookup("ConfigureServer");
            server=(Server)PortableRemoteObject.narrow(home.create(), Server.class);
        }
        
        return server;
    }

    public Version getVersion() throws Exception {
        return configureServer().getVersion();
    }

    public void resetCoreConfiguration() throws Exception {
        configureServer().resetCoreConfiguration();
    }

    public void resetNamedConfiguration(String name) throws Exception {
        configureServer().resetNamedConfiguration(name);
    }

    public void resetAllConfigurations() throws Exception {
        configureServer().resetNamedConfiguration("all");
    }

    public void clearConfigurationCache() throws Exception {
        configureServer().clearConfigurationCache();
    }

    public void clearNamedConfigurationCache(String namespace) throws Exception {
        configureServer().clearNamedConfigurationCache(namespace);
    }

    public String invokeServiceXML(String xml) throws Exception {
        return configureServer().invokeServiceXML(xml);
    }

    public GetNamespacesArg getNamespaces(GetNamespacesArg arg) throws Exception {
        return configureServer().getNamespaces(arg);
    }

    public GetConfigurationArg getConfiguration(GetConfigurationArg arg) throws Exception {
        return configureServer().getConfiguration(arg);
    }

    public SetConfigurationArg setConfiguration(SetConfigurationArg arg) throws Exception {
        return configureServer().setConfiguration(arg);
    }

    public GetCommandScriptArg getCommandScript(GetCommandScriptArg arg) throws Exception {
        return configureServer().getCommandScript(arg);
    }

    public SetCommandScriptArg setCommandScript(SetCommandScriptArg arg) throws Exception {
        return configureServer().setCommandScript(arg);
    }

    public GetClassListArg getClassList(GetClassListArg arg) throws Exception  {
        return configureServer().getClassList(arg);
    }

    public DeployCarArg deployCar(DeployCarArg arg) throws Exception  {
        return configureServer().deployCar(arg);
    }

    public PackageCarArg packageCar(PackageCarArg arg) throws Exception  {
        return configureServer().packageCar(arg);
    }

    public CatalogCarArg catalogCar(CatalogCarArg arg) throws Exception  {
        return configureServer().catalogCar(arg);
    }

    public Principal getUser() {
        return user;
    }
}
