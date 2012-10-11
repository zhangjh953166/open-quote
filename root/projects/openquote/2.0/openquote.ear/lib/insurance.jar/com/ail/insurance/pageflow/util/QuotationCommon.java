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
package com.ail.insurance.pageflow.util;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.insurance.pageflow.PageFlow;
import com.ail.insurance.pageflow.RowScroller;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.policy.Policy;

/**
 * This class defines methods which are common to both the quotation and sandpit Portlets.
 */
public class QuotationCommon {
    
    public static void processAction(ActionRequest request, ActionResponse response) throws XMLException {
    	PageFlow pageFlow=QuotationContext.getPageFlow();
    	Policy policy=QuotationContext.getPolicy();
        
        // apply values from the request back into the model
        pageFlow.applyRequestValues(request, response, policy);

        // if the request is immediate, or if the page passes validation...
        if (immediate(request) || !pageFlow.processValidations(request, response, policy)) {
            // ...process the actions in the request (e.g. move to next page)
            pageFlow.processActions(request, response, policy);

            // update the persisted quote
            persistQuote();
        }
    }

    public static void doView(RenderRequest request, RenderResponse response) throws IOException {
    	PageFlow pageFlow=QuotationContext.getPageFlow();
    	Policy policy=QuotationContext.getPolicy();
        
        pageFlow.renderResponse(request, response, policy);
    }

    /**
     * Get the name of the product we're quoting for. This comes from one of three places: in normal operation 
     * it is picked up from the portlet preference named 'product', or from the request property 
     * "openquote.product". In development mode it is picked up from the session.
     * @param session
     * @return the name of the product we're quoting for, or null if none is configured.
     */
    public static String productName(PortletRequest request) {
    	PortletSession session=request.getPortletSession();
    	PortletPreferences prefs=request.getPreferences();
    	
        String productName=prefs.getValue("product", (String)session.getAttribute("product"));

        if (request.getProperty("openquote.product")!=null) { 
        	productName=request.getProperty("openquote.product");
        }
        
        return productName;
    }

    /**
     * Fetch the productTypeID of the policy currently being quoted, or an empty String if
     * no product is currently being quoted.
     * @return ProductTypeId of the policy being processed, or "" if none is being processed.
     */
    public static String currentProduct() {
        if (QuotationContext.getPolicy()!=null && QuotationContext.getPolicy().getProductTypeId()!=null) {
            return QuotationContext.getPolicy().getProductTypeId();
        }
        else {
            return "";
        }
    }
    
    /**
     * Return true if this request is 'immediate'. Immediate requests are used to indicate that the actions
     * associated with the request should be immediately executed - even if the page would fail validation. This 
     * is useful, for example, when implementing a '&lt;&lt;Back' button on a page as it will allow the user
     * to move back even if the current page isn't fully filled in yet. It's also used by the {@link RowScroller}
     * to allow the 'Add' and 'Delete' buttons to work even when the contents of the scroller may not be valid. 
     * @return true if the request is immediate, false otherwise.
     */
    private static boolean immediate(ActionRequest request) {
        String im=Functions.getOperationParameters(request).getProperty("immediate");
        
        return "true".equals(im);
    }
    
    /**
     * Persist the current quotation. Take the quotation from the session, and persist
     * it to the database. If a record does not already exist, one is created; if one 
     * does exist, it is updated. Because persisting a object may modify it (a systemId
     * may be added, or a serial version incremented), the session's quotation object
     * is replaced. 
     * @param session
     * @throws XMLException
     */
    private static void persistQuote() throws XMLException {
        Policy quote=QuotationContext.getPolicy();

        if (quote!=null) {
        	quote=persistQuotation(quote);
        	QuotationContext.setPolicy(quote);
        }
    }

    public static Policy persistQuotation(Policy quote) {
        if (quote!=null) {
        	SavedPolicy saved=null;
        	CoreProxy proxy=new CoreProxy();
        	
            // Update it if we've saved it before, otherwise create it.
        	if (quote.isPersisted()) {
        		saved=(SavedPolicy)proxy.queryUnique("get.savedPolicy.by.systemId", quote.getSystemId());
        		saved.setPolicy(quote);
        		saved=proxy.update(saved);
            }
            else {
            	saved=new SavedPolicy(quote);
                saved=proxy.create(saved);
            }

            // Copy the system id and serial version number back into the quote for next time.
            quote.setSystemId(saved.getSystemId());
            quote.setSerialVersion(saved.getSerialVersion());
        }
        
        return quote;
    }
}
