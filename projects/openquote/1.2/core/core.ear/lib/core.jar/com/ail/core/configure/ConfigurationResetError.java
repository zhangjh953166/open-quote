/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

package com.ail.core.configure;


/**
 * This error is throw by PropertyLoaders when they fail due to some configuration
 * error.
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/10/01 17:12:38 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/ConfigurationResetError.java,v $
 */
public class ConfigurationResetError extends ConfigurationError {
    public ConfigurationResetError(String description) {
        super(description);
    }

    public ConfigurationResetError(String description, Throwable target) {
        super(description, target);
    }
}
