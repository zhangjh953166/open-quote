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

package com.ail.party;

import com.ail.core.Type;

/** @stereotype type */
public class Party extends Type {
    static final long serialVersionUID = -593625795936961828L;
    private String legalName;

    /**
     * @link aggregationByValue 
     */
    private Address address;

    public String getLegalName(){ return legalName; }

    public void setLegalName(String legalName){ this.legalName = legalName; }

    public Address getAddress(){ return address; }

    public void setAddress(Address address){ this.address = address; }
}
