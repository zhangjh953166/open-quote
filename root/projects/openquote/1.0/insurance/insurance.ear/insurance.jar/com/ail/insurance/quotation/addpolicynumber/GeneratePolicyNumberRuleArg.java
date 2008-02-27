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

package com.ail.insurance.quotation.addpolicynumber;

import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addpolicynumber/GeneratePolicyNumberRuleArg.java,v $
 * @stereotype arg
 */
public interface GeneratePolicyNumberRuleArg extends CommandArg {
    /**
     * Getter for the policyArg property. The policy to generate a number for
     * @return Value of policyArg, or null if it is unset
     */
    Policy getPolicyArg();

    /**
     * Setter for the policyArg property. * @see #getPolicyArg
     * @param policyArg new value for property.
     */
    void setPolicyArg(Policy policyArg);

    /**
     * Getter for the uniqueNumberArg property. A unique number that may be used by the number generation service. This number
     * is guaranteed to be unique for each invocation of the service.
     * @return Value of uniqueNumberArg, or null if it is unset
     */
    int getUniqueNumberArg();

    /**
     * Setter for the uniqueNumberArg property. * @see #getUniqueNumberArg
     * @param uniqueNumberArg new value for property.
     */
    void setUniqueNumberArg(int uniqueNumberArg);

    /**
     * Getter for the policyNumberRet property. The generated policy number.
     * @return Value of policyNumberRet, or null if it is unset
     */
    String getPolicyNumberRet();

    /**
     * Setter for the policyNumberRet property. * @see #getPolicyNumberRet
     * @param policyNumberRet new value for property.
     */
    void setPolicyNumberRet(String policyNumberRet);
}


