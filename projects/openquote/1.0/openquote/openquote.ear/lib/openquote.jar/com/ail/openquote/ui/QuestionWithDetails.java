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
package com.ail.openquote.ui;

import static com.ail.openquote.ui.util.Functions.renderAttribute;
import static com.ail.openquote.ui.util.Functions.xpathToId;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.Functions;

/**
 * <p>This element handles the common situation where selecting 'yes' in answer to a question
 * leads to the user having to answer a supplementary question (e.g. "If yes, please supply details").</p>
 * <p>This is a simplification of {@link QuestionWithSubSection QuestionWithSubSection} which 
 * isn't limited to a single supplementary question.</p>
 * <p><img src="doc-files/QuestionWithDetails.png"/></p>
 * <p>The main question must be a YesOrNo question, and must be phrased in such a way that "Yes" 
 * leads to more detail being required. As with the {@link Question Question} page element, the
 * detail question's answer field is rendered based on the properties of the {@link com.ail.core.Attribute Attribute}
 * that it is bound to.</p> 
 * <p>The detail field is only enabled if the question has been answered "Yes".</p>
 * <p>Validation is applied to the YesOrNo question, an answer is considered mandatory. Validation applied
 * to the detail question's answer is again defined by the properties of the {@link com.ail.core.Attribute Attribute}
 * that it is bound to.</p>  
 */
public class QuestionWithDetails extends Question {
    private static final long serialVersionUID = 7118438575837087257L;
    private String detailsTitle;
    private String detailsBinding;
    
    public QuestionWithDetails() {
		super();
	}

    public String getDetailsBinding() {
        return detailsBinding;
    }

    public void setDetailsBinding(String detailsBinding) {
        this.detailsBinding = detailsBinding;
    }

    public String getDetailsTitle() {
        return detailsTitle;
    }

    public void setDetailsTitle(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

    @Override
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        applyRequestValues(request, response, model, "");
    }

    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
        super.applyRequestValues(request, response, model, rowContext);
        Functions.applyAttributeValues(model, getDetailsBinding(), rowContext, request);
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    boolean error=false;

	    // validate the yes/no part of the question
        error|=super.processValidations(request, response, model);

        // if the question's answer is 'Yes', and the detail is empty...
        if ("Yes".equals(model.xpathGet(getBinding()+"/value"))) {
            error|=Functions.applyAttributeValidation(model, getDetailsBinding());
        }

        return error;
    }

	@Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    renderResponse(request, response, model, "");
    }

    @Override
    public void renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        String title=getTitle();
        String questionId=xpathToId(rowContext+binding);
        String detailsId=xpathToId(rowContext+detailsBinding);
        
        PrintWriter w=response.getWriter();

        String onChange="enableTargetIf(this.value==\"Yes\", \""+detailsId+"\")";
        
        if (title==null && getTitleBinding()!=null) {
            title=model.xpathGet(getTitleBinding(), String.class);
        }
        
        w.printf("<td>%s</td>", getTitle());
        w.printf("<td>%s</td>", renderAttribute(model, getBinding(), rowContext, onChange, getOnLoad()));
        w.printf("<td>%s</td>", getDetailsTitle());
        w.printf("<td>%s</td>", renderAttribute(model, getDetailsBinding(), rowContext, getOnChange(), getOnLoad()));
        
        // Disable the 'detail' textarea unless the question's answer is 'Yes'
        w.printf("<script type='text/javascript'>enableTargetIf(document.getElementsByName(\"%s\")[0].value==\"Yes\", \"%s\")</script>",
                questionId, detailsId);
    }
}
