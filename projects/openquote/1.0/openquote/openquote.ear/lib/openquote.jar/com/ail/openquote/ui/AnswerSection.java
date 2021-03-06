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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;

/**
 * <p>An AnswerSection is generally used on summary pages and acts as a container for listing the answers
 * given to previously asked questions. It contains a list of {@link #getAnswer() answers} which may be
 * instances of {@link Answer} or {@link AnswerScroller}.</p>
 * <p><img src="doc-files/AnswerSection.png"/></p>
 * <p>The screenshot shows two answer sections. The first contains 5 {@link Answer Answers}; the second
 * contains a single {@link AnswerScroller} which itself contains 3 {@link Answer Answers}.</p>
 * <p>The {@link #getTitle() title} may be static (using {@link #setTitle(String)}, or dynamic (using {@link #setTitleBinding(String)}.
 * Dynamic titles are defined as XPath expressions which are evaluated at page render time against the quotation model. The
 * result of the evaluation is used as the title.
 * @see Answer
 * @see AnswerScroller
 */
public class AnswerSection extends PageElement {
	private static final long serialVersionUID = 6794522768423045427L;
    
    /** List of things to be rendered in the section */
    private ArrayList<? extends Answer> answer; 

    /** The fixed title to be displayed with the answer */
    private String title;

    /** 
     * An optional XPath value which can be evaluated against the quotation to fetch a dynamic title.
     * If defined this is used in preference to the fixed title: @{link #title} 
     */
    private String titleBinding;
    
    public AnswerSection() {
        super();
        answer=new ArrayList<Answer>();
    }
    
    /**
     * List of things ({@link Answer Answers} and {@link AnswerSection AnswerSections} to be rendered 
     * in the section. 
     * @return List of contained elements.
     */
    public ArrayList<? extends Answer> getAnswer() {
        return answer;
    }

    /**
     * @see #getAnswer()
     * @param answer
     */
    public void setAnswer(ArrayList<? extends Answer> answer) {
        this.answer = answer;
    }

    /**
     * The fixed title to be displayed with the answer.
     * @return value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @see #getTitle()
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * An optional XPath value which can be evaluated against the quotation to fetch a dynamic title.
     * If defined this is used in preference to the fixed title: @{link #title} 
     * @return Title's XPath binding
     */
    public String getTitleBinding() {
        return titleBinding;
    }

    /**
     * @see #getTitleBinding()
     * @param titleBinding
     */
    public void setTitleBinding(String titleBinding) {
        this.titleBinding = titleBinding;
    }

    @Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w = response.getWriter();
        w.printf(" <table width='100%%' border='0' cols='2'>");

        String aTitle=(titleBinding!=null) ? (String)model.xpathGet(titleBinding) : title;

        // output the title row if a title was defined
        if (aTitle!=null) {
            w.printf("  <tr class='portlet-section-subheader'><td colspan='2'><u>%s</u></td></tr>", aTitle);
        }

        for(Answer a: answer) { 
            a.renderResponse(request, response, model);
        }
        w.printf("</table>");
	}
}
