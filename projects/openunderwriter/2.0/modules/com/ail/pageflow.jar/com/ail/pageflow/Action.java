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
package com.ail.pageflow;

import static com.ail.pageflow.ActionType.ON_APPLY_REQUEST_VALUES;
import static com.ail.pageflow.ActionType.ON_PROCESS_ACTIONS;
import static com.ail.pageflow.ActionType.ON_PROCESS_VALIDATIONS;
import static com.ail.pageflow.ActionType.ON_RENDER_RESPONSE;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.ExecutePageActionService.ExecutePageActionCommand;
import com.ail.pageflow.service.PersistPolicyService.PersistPolicyCommand;

/**
 * Actions allow arbitrary commands to be invoked during a page flow. A number
 * of page elements (e.g. {@link Page} and {@link CommandButtonAction}) allow
 * Actions to be associated with them and will invoke the commands they define
 * in response to events.</p> For example, any number of Actions can be attached
 * to a CommandButtonAction. When the button is selected all the commands
 * associated with the actions are executed (see {@link #condition}). If the
 * commands execute successfully, the quote's persisted database record is
 * automatically updated to keep it in sync with any changes that the command
 * may have made.
 */
public class Action extends PageElement {
	private static final long serialVersionUID = -1320299722728499324L;
	private ActionType when = ON_PROCESS_ACTIONS;

	/**
	 * The name of the command to be invoked. This command must be a service
	 * which implements
	 * {@link com.ail.core.product.executepageaction.ExecutePageActionCommand
	 * ExecutePageActionCommand}.
	 */
	private String commandName;

	public Action() {
		super();
	}

	public Action(ActionType when, String commandName, String condition) {
		super(condition);
		this.when = when;
		this.commandName = commandName;
	}
    
	@Override
	public Type processActions(ActionRequest request, ActionResponse response, Type model) {
		if (conditionIsMet(model)) {
			model = executeAction(request, model, ON_PROCESS_ACTIONS);
		}
		
		return model;
	}

	@Override
	public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
		if (conditionIsMet(model)) {
			model = executeAction(request, model, ON_APPLY_REQUEST_VALUES);
		}

		return model;
	}

	@Override
	public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
		if (conditionIsMet(model)) {
			return executeValidation(request, model, ON_PROCESS_VALIDATIONS);
		} else {
			return false;
		}
	}

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    return executeAction(request, model, ON_RENDER_RESPONSE);
	}

	/**
	 * Get the action's command
	 * 
	 * @see #commandName
	 * @return The action's command name
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * Set the actions command name
	 * 
	 * @see #commandName
	 * @param commandName
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	/**
	 * Get the action's when value
	 * 
	 * @see #when
	 * @return The action's when value
	 */
	public ActionType getWhen() {
		return when;
	}

	/**
	 * Set the action's when value
	 * 
	 * @see #when
	 * @param when
	 *            When value
	 */
	public void setWhen(ActionType when) {
		this.when = when;
	}

	public Type executeAction(PortletRequest portletRequest, Type model, ActionType currentPhase) {
	    if (when.equals(currentPhase)) {
    	    Policy policy = (Policy)execute(portletRequest, model).getModelArgRet();
    
    		// Always persist the quote after running an action - the next
    		// action/command may need to read the persisted state.
    	    PersistPolicyCommand persistPolicy=PageFlowContext.getCoreProxy().newCommand(PersistPolicyCommand.class);
    	    persistPolicy.setPolicyArgRet(policy);

    	    try {
    	        persistPolicy.invoke();
            } catch (Throwable e) {
                throw new RenderingError("Failed to updated persisted model: " + getCommandName(), e);
            } 

    	    return persistPolicy.getPolicyArgRet();
	    }
	    else {
	        return model;
	    }
	}

	private boolean executeValidation(PortletRequest portletRequest, Type model, ActionType currentPhase) {
        if (when.equals(currentPhase)) {
            return execute(portletRequest, model).getValidationFailedRet();
        }
        else {
            return false;
        }
	}

    private ExecutePageActionCommand execute(PortletRequest portletRequest, Type model) {
		Policy quote = (Policy) model;
		
		ExecutePageActionCommand c = PageFlowContext.getCoreProxy().newCommand(ExecutePageActionCommand.class);

        c.setActionArg(this);
        c.setServiceNameArg(commandName);
		c.setModelArgRet(quote);
        c.setPortletRequestArg(portletRequest);
		c.setPortletSessionArg(portletRequest.getPortletSession());
		c.setPortletPreferencesArg(portletRequest.getPreferences());
		c.setRequestParameterArg(portletRequest.getParameterMap());
		
		try {
			c.invoke();
		} catch (Throwable e) {
			throw new RenderingError("Failed to execute action command: " + getCommandName(), e);
		} 

		return c;
	}
}
