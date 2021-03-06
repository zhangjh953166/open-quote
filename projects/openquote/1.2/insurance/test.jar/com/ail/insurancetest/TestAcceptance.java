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

package com.ail.insurancetest;


import junit.framework.Test;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.product.listproducts.ListProductsService;
import com.ail.coretest.CoreUserTestCase;
import com.ail.financial.Currency;
import com.ail.financial.CurrencyAmount;
import com.ail.financial.PaymentSchedule;
import com.ail.insurance.acceptance.AcceptanceBean;
import com.ail.insurance.acceptance.CollectPremiumCommand;
import com.ail.insurance.acceptance.PolicyDocumentation;
import com.ail.insurance.acceptance.ProduceDocumentationCommand;
import com.ail.insurance.acceptance.PutOnRiskCommand;
import com.ail.insurance.acceptance.acceptquotation.AcceptQuotationCommand;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberService;

/**
 * @version $Revision: 1.6 $
 * @state $State: Exp $
 * @date $Date: 2007/06/10 11:05:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/test.jar/com/ail/insurancetest/TestAcceptance.java,v $
 */
public class TestAcceptance extends CoreUserTestCase {
    private static final long serialVersionUID = -1883228598369537657L;


    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestAcceptance(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestAcceptance.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

	/**
	 * Sets up the fixture (run before every test).
	 * Get an instance of Core, and delete the testnamespace from the config table.
	 */
	protected void setUp() {
	    super.setupSystemProperties();
        
        ConfigurationHandler.reset();
        setVersionEffectiveDate(new VersionEffectiveDate());
        tidyUpTestData();
        setCore(new Core(this));
        getCore().resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        
        resetConfiguration();
        new AcceptanceBean().resetConfiguration();
        new AddPolicyNumberService().resetConfiguration();        
        new ListProductsService().resetConfiguration();
        
        ConfigurationHandler.reset();
        setVersionEffectiveDate(new VersionEffectiveDate());
	}

	/**
	 * Tears down the fixture (run after each test finishes)
	 */
	protected void tearDown() {
		tidyUpTestData();
	}


	/**
	 * Test put on risk from quotation status
	 * @throws Exception
	 */
	public void testPutOnRiskSuccess(){

        Policy policy = (Policy)getCore().newProductType("com.ail.core.product.TestProduct1");

		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.QUOTATION);

		// run command
		PutOnRiskCommand command = (PutOnRiskCommand) getCore().newCommand("PutOnRiskService");
		command.setPolicyArgRet(policy);

		try {
			command.invoke();
		} catch (BaseException e) {
			e.printStackTrace();
			fail("put on risk failed");
		}
		policy = command.getPolicyArgRet();

		// check that the staus is set
		assertNotNull(policy);
		assertNotNull(policy.getStatus());
		assertEquals(PolicyStatus.ON_RISK, policy.getStatus());

	}

	/**
	 * Test put on risk from refer status
	 * @throws Exception
	 */
	public void testPutOnRiskFail() throws Exception {

		// create policy
		Policy policy = new Policy();
		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.REFERRED);

		// run command
		PutOnRiskCommand command = (PutOnRiskCommand) getCore().newCommand("PutOnRiskService");
        command.setPolicyArgRet(policy);

        try {
			command.invoke();
		} catch (PreconditionException e) {
		    // this is what we like
        }
	}

	/**
	 * Test collect premium from on risk status & payment details
	 * @throws Exception
	 */
	public void testCollectPremiumSuccess(){

		// create policy
		Policy policy = new Policy();
		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.ON_RISK);
		PaymentSchedule payment = new PaymentSchedule();
		policy.setPaymentDetails(payment);

		// run command
		CollectPremiumCommand command = (CollectPremiumCommand) getCore().newCommand("CollectPremiumService");
		command.setPolicyArg(policy);
		try {
			command.invoke();
		} catch (BaseException e) {
			e.printStackTrace();
			fail("collect premium failed");
		}

	}


	/**
	 * Test collect premium from on risk status & pre payment
	 * @throws Exception
	 */
	public void testCollectPremiumSuccessPrePayemt(){

		// create policy
		Policy policy = new Policy();
		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.ON_RISK);

		// create prepayment
		CurrencyAmount amount = new CurrencyAmount();
		amount.setAmountAsString("100");
		amount.setCurrencyAsString(Currency.GBP.name());

		// run command
		CollectPremiumCommand command = (CollectPremiumCommand) getCore().newCommand("CollectPremiumService");
		command.setPolicyArg(policy);
        command.setPrePaymentArg(amount);
		try {
			command.invoke();
		} catch (BaseException e) {
			e.printStackTrace();
			fail("collect premium failed");
		}

	}


	/**
	 * Test collect premium from on risk status & no payment details
	 * @throws Exception
	 */
	public void testCollectPremiumFail() throws Exception {

		// create policy
		Policy policy = new Policy();
		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.ON_RISK);

		// run command
		CollectPremiumCommand command = (CollectPremiumCommand) getCore().newCommand("CollectPremiumService");
		command.setPolicyArg(policy);
		try {
			command.invoke();
            fail("collect premium should fail due to no payment details");
		} catch (PreconditionException e) {
		    // ignore this - it's what we're looking for
        }
	}

	/**
	 * Test produce documentation from on risk status
	 * @throws Exception
	 */
	public void testProduceDocumentationSuccess() throws Exception {

		// create policy
		Policy policy = new Policy();
		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.ON_RISK);

		// run command
		ProduceDocumentationCommand command = (ProduceDocumentationCommand) getCore().newCommand("ProduceDocumentationService");
		command.setPolicyArg(policy);
		command.invoke();

		// check that the documentation is set
		PolicyDocumentation docs = command.getDocumentationRet();
		assertNotNull(docs);
	}


	/**
	 * Test produce documentation from refer staus
	 * @throws Exception
	 */
	public void testProduceDocumentationFail() throws Exception {

		// create policy
		Policy policy = new Policy();
		policy.setId("pol1");
		policy.setPolicyNumber("pol1");
		policy.setStatus(PolicyStatus.REFERRED);

		// run command
		ProduceDocumentationCommand command = (ProduceDocumentationCommand) getCore().newCommand("ProduceDocumentationService");
		command.setPolicyArg(policy);
		try {
		    command.invoke();
        }
        catch(PreconditionException e) {
            // this is what we want
        }
	}

	public void testAcceptQuotationSuccess() throws Exception {
        // create policy
        Policy policy = new Policy();
        policy.setStatus(PolicyStatus.QUOTATION);
        policy.setPaymentDetails(new PaymentSchedule());

        AcceptQuotationCommand cmd=(AcceptQuotationCommand)getCore().newCommand("AcceptQuotation");
        cmd.setPolicyArgRet(policy);
        cmd.invoke();
        
        assertTrue(PolicyStatus.SUBMITTED.equals(policy.getStatus()));
    }
}
