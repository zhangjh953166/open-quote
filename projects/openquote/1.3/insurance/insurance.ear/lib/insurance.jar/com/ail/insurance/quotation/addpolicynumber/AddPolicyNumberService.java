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

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;

/**
 * @version $Revision: 1.7 $
 * @state $State: Exp $
 * @date $Date: 2007/06/10 11:05:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addpolicynumber/AddPolicyNumberService.java,v $
 * @stereotype service
 */
public class AddPolicyNumberService extends Service {
    private static final long serialVersionUID = 4156690077014872967L;
    private AddPolicyNumberArg args = null;
    private Core core = null;
    private static UniqueNumberHandler uniqueNumberHandler=new UniqueNumberHandler();
    private String configurationNamespace="com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberService";

    
    /** Default constructor */
    public AddPolicyNumberService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2007/06/10 11:05:59 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addpolicynumber/AddPolicyNumberService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.7 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (AddPolicyNumberArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of AddPolicyNumberArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    public String getConfigurationNamespace() {
        return configurationNamespace;
    }

    /** 
     * This service adds a policy number to a policy. The specific format and content of the policy number depends on 
     * business rules, and may vary depending the policy's product.
     * 
     * The service checks the preconditions, and then generates a unique number before passing control into the product
     * specific 'GeneratePolicyNumberRule'. The policy and unique number are passed into this rule. The rule isn't obliged
     * to use the unique number, and may generate other numbers of it's own, but it is expected to set the policy's policy
     * number to something other than null or '' (see the postconditions).
     * 
     * The unique number generated by this service is unique in a system wide sence. The numbers it generates aren't
     * necessarily contiguous and (as mentioned above) they may never be used. Numbers are allocated to instances of
     * this service in a block by block basis. The service's configuration defines the last number allocated, and the
     * size of the allocation blocks. The last number allocated is updated every time a new block is issued.
     * 
     * @preconditions args.getPolicyArgRet()!=null
     *                args.getPolicyArgRet().getStatus()!=null
     *                args.getPolicyArgRet().getStatus().equals(PolicyStatus.Quotation)
     *                args.getPolicyArgRet().getPolicyNumber()==null || args.getPolicyArgRet().getPolicyNumber().length()==0
     *                args.getPolicyArgReg().getProdctTypeId()!=null
     * @postconditions args.getPolictArgRet().getPolicyNumber()!=null && args.getPolicyArgRet().getPolicyNumber().length()!=0
     */
    public void invoke() throws PreconditionException, PostconditionException, BaseException {
        // Select this service's configuration
        configurationNamespace="com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberService";

        if (args.getPolicyArgRet()==null) {
            throw new PreconditionException("args.getPolicyArgRet()==null");
        }

        Policy policy=args.getPolicyArgRet();

        if (policy.getStatus()==null) {
            throw new PreconditionException("policy.getStatus()==null");
        }

        if (!policy.getStatus().equals(PolicyStatus.QUOTATION)) {
            throw new PreconditionException("policy.getStatus()!=PolicyStatus.Quotation");
        }

        if (policy.getPolicyNumber()!=null && policy.getPolicyNumber().trim().length()!=0) {
            throw new PreconditionException("policy.getPolicyNumber()!=null && policy.getPolicyNumber().trim().length()!=0");
        }

        if (policy.getProductTypeId()==null) {
            throw new PreconditionException("policy.getProductTypeId()==null");
        }

        // get the next unique number
        synchronized(uniqueNumberHandler) {
            if (uniqueNumberHandler.isBlockEmpty()) {
                
                Configuration config=getConfiguration();
                int nextNumber=Integer.parseInt(config.findParameter("NextNumber").getValue());
                int blockSize=Integer.parseInt(config.findParameter("BlockSize").getValue());

                uniqueNumberHandler.setNextNumber(nextNumber);
                uniqueNumberHandler.setBlockEnd(nextNumber+blockSize);

                config.findParameter("NextNumber").setValue(Integer.toString(nextNumber+blockSize+1));

                setConfiguration(config);
            }
        }

        // switch over to the product's configuration
        configurationNamespace=Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());

        GeneratePolicyNumberRuleCommand command=(GeneratePolicyNumberRuleCommand)core.newCommand("GeneratePolicyNumberRule");
        command.setPolicyArg(policy);
        command.setUniqueNumberArg(uniqueNumberHandler.getNextNumber());
        command.invoke();
        String policyNumber=command.getPolicyNumberRet();
        core.logDebug("Policy number: "+policyNumber+" generated");
        args.getPolicyArgRet().setPolicyNumber(policyNumber);
        
        if (policy.getPolicyNumber()==null || policy.getPolicyNumber().length()==0) {
            throw new PostconditionException("policy.getPolicyNumber()==null || policy.getPolicyNumber().length()==0");
        }
    }
}

/**
 * Private class used to track the usage of unique numbers.
 */
class UniqueNumberHandler {
    private int blockEnd=0;
    private int nextNumber=0;

    public boolean isBlockEmpty() {
        return blockEnd==nextNumber;
    }

    public int getNextNumber() {
        return nextNumber++;
    }

    public void setNextNumber(int next) {
        nextNumber=next;
    }

    public void setBlockEnd(int blockEnd) {
        this.blockEnd=blockEnd;
    }
}

