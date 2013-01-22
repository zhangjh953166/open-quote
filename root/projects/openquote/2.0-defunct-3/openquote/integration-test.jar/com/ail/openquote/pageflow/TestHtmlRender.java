/* Copyright Applied Industrial Logic Limited 2006. All rights reserved. */
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

package com.ail.openquote.pageflow;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.XMLString;
import com.ail.insurance.pageflow.PageFlow;
import com.ail.insurance.pageflow.util.QuotationContext;
import com.ail.insurance.policy.Policy;

/**
 */
public class TestHtmlRender extends CoreUserBaseCase {
    private static final long serialVersionUID = 2030295330203910171L;

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        super.setupSystemProperties();
        super.setCore(new Core(this));
    }

    @Test
    public void testPageRender() throws Exception {
        XMLString instanceXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusInstance.xml"));
        Policy instanceObj = (Policy) super.getCore().fromXML(Policy.class, instanceXml);
        assertNotNull(instanceObj);

        XMLString pageFlowXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusPageFlow.xml"));
        PageFlow pageFlowObj = super.getCore().fromXML(PageFlow.class, pageFlowXml);
        assertNotNull(pageFlowObj);
        
        StringWriter output=new StringWriter();
        
        PortletSession session=new MockPortletSession(instanceObj);
        RenderResponse response=new MockRenderResponse(Locale.UK, new PrintWriter(output));
        RenderRequest request=new MockRenderRequest(Locale.UK, session);
        
        QuotationContext.setRequest(request);
        QuotationContext.setPolicy(instanceObj);
        QuotationContext.setPageFlow(pageFlowObj);
        
        pageFlowObj.renderResponse(request, response, instanceObj);
        
        System.out.println(output.getBuffer());
    }
}
