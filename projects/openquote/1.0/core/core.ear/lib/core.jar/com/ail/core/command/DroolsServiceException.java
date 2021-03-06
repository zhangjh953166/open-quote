/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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
package com.ail.core.command;

import com.ail.core.BaseException;

/**
 * This exception is thrown by the Drools Accessor in response to exceptions thrown
 * by drools itself. It acts as a simple wrapper.
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/11/25 09:11:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/command/DroolsServiceException.java,v $
 */
public class DroolsServiceException extends BaseException {
    public DroolsServiceException(Exception e) {
        super(e.toString(), e);
    }
}
