/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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

package com.ail.insurance.quotation.notifyparty;

import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;

/**
 * Service to notify a party of the existance of a quote. Typical implementations of this service include
 * notifying proposers, brokers and/or carriers of the creation of a new quotation. The implementation might
 * send notification by a number of channels including emails and web service calls.</p>
 * The caller may specify the quotation either by quote number or by the instance of the quote itself.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype arg
 */
public interface NotifyPartyArg extends CommandArg {
    String getQuotationNumberArg();
    
    void setQuotationNumberArg(String quotationNumberArg);
    
    Policy getPolicyArg();
    
    void setPolicyArg(Policy policyArg);
}
