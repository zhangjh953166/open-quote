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

import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;

/**
 * Arg interface for the GetConfiguration entry point. The entry point takes one
 * argument: a namespace's name, and returns one result: the Configuration object
 * for the namespace. 
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/GetConfigurationArg.java,v $
 */
public interface GetConfigurationArg extends CommandArg {
    void setConfigurationRet(Configuration configurationRet);

    Configuration getConfigurationRet();

    void setNamespaceArg(String namespace);

    String getNamespaceArg();
}
