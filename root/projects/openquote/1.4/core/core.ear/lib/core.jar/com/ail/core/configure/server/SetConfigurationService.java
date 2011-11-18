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

import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.configure.ConfigurationHandler;

public class SetConfigurationService extends Service<SetConfigurationArg> {
    @Override
    public String getConfigurationNamespace() {
        return args.getNamespaceArg();
    }

    @Override
    public void invoke() throws PreconditionException {
        if (args.getNamespaceArg()==null || args.getNamespaceArg().length()==0) {
            throw new PreconditionException("args.getNamespaceArg()==null || args.getNamespaceArg().length()==0");
        }

        if (args.getConfigurationArg()==null) {
            throw new PreconditionException("args.getConfigurationArg()==null");
        }

        ConfigurationHandler.getInstance().saveConfiguration(this, args.getConfigurationArg(), core);
    }
}

