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

package com.ail.core.logging;

import com.ail.core.Functions;
import com.ail.core.TypeEnum;

/**
 * This class represents an enumeration of logging severity levels.
 * @version $Revision: 1.6 $
 * @state $State: Exp $
 * @date $Date: 2006/01/16 22:54:22 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/logging/Severity.java,v $
 * @stereotype enumeration
 */
public enum Severity implements TypeEnum {
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    FATAL;
    
    private final String longName;
    
    Severity() {
        this.longName=name();
    }
    
    Severity(String longName) {
        this.longName=longName;
    }
    
    public String valuesAsCsv() {
        return Functions.arrayAsCsv(values());
    }

    public String longName() {
        return longName;
    }
    
    /**
     * This method is similar to the valueOf() method offered by Java's Enum type, but
     * in this case it will match either the Enum's name or the longName.
     * @param name The name to lookup
     * @return The matching Enum, or IllegalArgumentException if there isn't a match.
     */
    public static Severity forName(String name) {
        return (Severity)Functions.enumForName(name, values());
    }

    public String getName() {
        return name();
    }
    
    public String getLongName() {
        return longName();
    }
    
    public int getOrdinal() {
        return ordinal();
    }
}