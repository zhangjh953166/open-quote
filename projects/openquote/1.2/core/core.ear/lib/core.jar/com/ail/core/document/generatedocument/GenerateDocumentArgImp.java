/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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

package com.ail.core.document.generatedocument;

import com.ail.core.Type;
import com.ail.core.command.CommandArgImp;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/09/15 21:06:57 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/document/generatedocument/GenerateDocumentArgImp.java,v $
 * @stereotype argimp
 */
public class GenerateDocumentArgImp extends CommandArgImp implements GenerateDocumentArg {
    public String getProductNameArg() {
        return productNameArg;
    }

    public void setProductNameArg(String productNameArg) {
        this.productNameArg=productNameArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Type getModelArg() {
        return modelArg;
    }

    /**
     * {@inheritDoc}
     * @param modelArg @{inheritDoc}
     */
    public void setModelArg(Type modelArg) {
        this.modelArg = modelArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getDocumentDefinitionArg() {
        return documentDefinitionArg;
    }

    /**
     * {@inheritDoc}
     * @param keyArg @{inheritDoc}
     */
    public void setDocumentDefinitionArg(String documentDefinitionArg) {
        this.documentDefinitionArg = documentDefinitionArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public byte[] getRenderedDocumentRet() {
        return renderedDocumentRet;
    }

    /**
     * {@inheritDoc}
     * @param renderedDocumentRet @{inheritDoc}
     */
    public void setRenderedDocumentRet(byte[] renderedDocumentRet) {
        this.renderedDocumentRet = renderedDocumentRet;
    }

    static final long serialVersionUID = 1199346453402049909L;

    /** The XML representation of the dynamic data required to render the document */
    private Type modelArg;

    /** Defines the type of document to be rendered */
    private String documentDefinitionArg;

    /** The name of the product for which the document is being generated */
    private String productNameArg;
    
    /** The result of the rendering process */
    private byte[] renderedDocumentRet;

    /** Default constructor */
    public GenerateDocumentArgImp() {
    }
}


