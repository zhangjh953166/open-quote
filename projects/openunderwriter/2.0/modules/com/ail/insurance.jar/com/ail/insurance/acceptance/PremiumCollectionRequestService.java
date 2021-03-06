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

package com.ail.insurance.acceptance;

import static com.ail.financial.MoneyProvisionStatus.NEW;
import static com.ail.financial.PaymentRecordType.REQUESTED;
import static com.ail.insurance.policy.PolicyStatus.ON_RISK;

import java.net.URL;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.financial.MoneyProvision;
import com.ail.financial.MoneyProvisionStatus;
import com.ail.financial.PayPal;
import com.ail.financial.PaymentRecord;
import com.ail.insurance.policy.Policy;
import com.ail.payment.PaymentRequestService.PaymentRequestCommand;

@ServiceImplementation
public class PremiumCollectionRequestService extends com.ail.core.Service<PremiumCollectionRequestService.PremiumCollectionRequestArgument> {
    private static final long serialVersionUID = 1871676649916485145L;

    @ServiceArgument
    public interface PremiumCollectionRequestArgument extends Argument {
        /**
         * Getter for the policyArg property. Policy to collect premium for
         * 
         * @return Value of policyArg, or null if it is unset
         */
        Policy getPolicyArgRet();

        /**
         * Setter for the policyArg property. * @see #getPolicyArg
         * 
         * @param policyArg
         *            new value for property.
         */
        void setPolicyArgRet(Policy policyArg);

        /**
         * The URL returned by the service which must be used to authorise the
         * payment. The client of this service will typically need to forward
         * the user to this URL in order for them to authorise the payment.
         */
        URL getAuthorisationURLRet();

        /**
         * @see #getAuthorisationURLRet()
         * @param authorisationURLRet
         */
        void setAuthorisationURLRet(URL authorisationURLRet);

        /**
         * The URL to which the user should be forwarded if the payment process
         * is cancelled.
         */
        URL getCancelledURLArg();

        /**
         * @see #getCancelledURLArg()
         * @param cancelledURLArg
         */
        void setCancelledURLArg(URL cancelledURLArg);

        /**
         * URL to forward the user to when they payment is authorised and
         * approved.
         */
        URL getApprovedURLArg();

        /**
         * @see #getApprovedURLArg()
         * @param approvedURLArg
         */
        void setApprovedURLArg(URL approvedURLArg);

        /**
         * Fetch the request ID. This is the ID that the payment provider gave to the request.
         */
        String getPaymentIdRet();

        /**
         * @see #getPaymentIdRet()
         * @param paymentIdRet
         */
        void setPaymentIdRet(String paymentIdRet);
    }

    @ServiceCommand(defaultServiceClass = PremiumCollectionRequestService.class)
    public interface PremiumCollectionRequestCommand extends Command, PremiumCollectionRequestArgument {
    }

    @Override
    public void invoke() throws BaseException {

        if (args.getPolicyArgRet() == null) {
            throw new PreconditionException("args.getPolicyArg()==null");
        }

        if (!ON_RISK.equals(args.getPolicyArgRet().getStatus())) {
            throw new PreconditionException("!ON_RISK.equals(args.getPolicyArg().getStatus())");
        }

        if (args.getPolicyArgRet().getPaymentDetails() == null) {
            throw new PreconditionException("args.getPolicyArg().getPaymentOption()==null");
        }

        if (args.getPolicyArgRet().getPaymentDetails().getMoneyProvision() == null) {
            throw new PreconditionException("args.getPolicyArg().getPaymentDetails().getMoneyProvision() == null");
        }

        if (args.getPolicyArgRet().getPaymentDetails().getMoneyProvision().size() == 0) {
            throw new PreconditionException("args.getPolicyArg().getPaymentDetails().getMoneyProvision().size() == 0");
        }
        
        if (args.getPolicyArgRet().getProductTypeId() == null || args.getPolicyArgRet().getProductTypeId().length() == 0) {
            throw new PreconditionException("args.getPolicyArg().getProductTypeId() == null || args.getPolicyArg().getProductTypeId().length() == 0");
        }

        Policy policy = args.getPolicyArgRet();

        // Switch the to the product's configuration
        String namespace = Functions.productNameToConfigurationNamespace(policy.getProductTypeId());
        setCore(new CoreProxy(namespace, args.getCallersCore()).getCore());

        for (MoneyProvision mp : policy.getPaymentDetails().getMoneyProvision()) {
            if (mp.getPaymentMethod() instanceof PayPal) {
                if (NEW.equals(mp.getStatus())) {
                    PaymentRequestCommand prc = core.newCommand("BuyWithPayPalCommand", PaymentRequestCommand.class);
                    prc.setProductTypeIdArg(policy.getProductTypeId());
                    prc.setDescriptionArg(mp.getDescription());
                    prc.setAmountArg(mp.getAmount());
                    prc.setApprovedURLArg(args.getApprovedURLArg());
                    prc.setCancelledURLArg(args.getCancelledURLArg());
                    prc.invoke();
                    args.setAuthorisationURLRet(prc.getAuthorisationURLRet());
                    args.setPaymentIdRet(prc.getPaymentIdRet());
                    
                    mp.setPaymentId(prc.getPaymentIdRet());
                    mp.setStatus(MoneyProvisionStatus.REQUESTED);
                    
                    policy.getPaymentHistory().add(new PaymentRecord(mp.getAmount(), prc.getPaymentIdRet(), mp.getPaymentMethod(), REQUESTED));
                }
            } else {
                throw new PreconditionException("This service is limited to handling PayPal payments.");
            }
        }
    }
}
