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
package com.ail.core.xmlbinding;

import org.apache.commons.beanutils.PropertyUtils;
import org.exolab.castor.mapping.AbstractFieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;

import com.ail.core.XMLString;


public class CastorXMLStringFieldHandler extends AbstractFieldHandler {

    public Object getValue(Object object) throws IllegalStateException {
        FieldDescriptor f = getFieldDescriptor();
        String fieldName = f.getFieldName();
        String value = null;
        
        try {
            value = (String)PropertyUtils.getProperty(object, fieldName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        if (value==null || value.length()==0) {
            return value;
        }
        
        return new XMLString(value.toString()).toStringWithEntityReferences(false);
    }

    public Object newInstance(Object arg0) throws IllegalStateException {
        return new String();
    }

    public Object newInstance(Object arg0, Object arg1[]) throws IllegalStateException {
        return new String();
    }

    public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
        setValue(object, "");     
    }

    public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
        FieldDescriptor f = getFieldDescriptor();
        String fieldName = f.getFieldName();

        try {
            String v = new XMLString(value.toString()).toStringWithoutEntityReferences();
            PropertyUtils.setProperty(object, fieldName, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
