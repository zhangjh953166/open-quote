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
import com.ail.core.CommandScript;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/GetCommandScriptArg.java,v $
 */
public interface GetCommandScriptArg extends CommandArg {
    /**
     * Fetch the value of the namespaceArg argument. The namespace to fetch the command script from.
     * @see #setNamespaceArg
     * @return value of namespaceArg
     */
    String getNamespaceArg();

    /**
     * Set the value of the namespaceArg argument. The namespace to fetch the command script from.
     * @see #getNamespaceArg
     * @param namespaceArg New value for namespaceArg argument.
     */
    void setNamespaceArg(String namespaceArg);

    /**
     * Fetch the value of the commandNameArg argument. The name of the command that which the script should be returned for.
     * @see #setCommandNameArg
     * @return value of commandNameArg
     */
    String getCommandNameArg();

    /**
     * Set the value of the commandNameArg argument. The name of the command that which the script should be returned for.
     * @see #getCommandNameArg
     * @param commandNameArg New value for commandNameArg argument.
     */
    void setCommandNameArg(String commandNameArg);

    /**
     * Fetch the value of the commandScriptRet argument. The command script returned.
     * @see #setCommandScriptRet
     * @return value of commandScriptRet
     */
    CommandScript getCommandScriptRet();

    /**
     * Set the value of the commandScriptRet argument. The command script returned.
     * @see #getCommandScriptRet
     * @param commandScriptRet New value for commandScriptRet argument.
     */
    void setCommandScriptRet(CommandScript commandScriptRet);
}


