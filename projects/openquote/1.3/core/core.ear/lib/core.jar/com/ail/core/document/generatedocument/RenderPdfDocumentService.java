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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
        

/**
 * This class provides an implementation of the render document service which renders to PDF
 * using Apache FOP.<p/>
 * The render options supported by this implementation match up one-to-one with those supported
 * by the Apache FOP renderer:<ul>
 * <li>allowCopyContent</li>
 * <li>allowEditContent</li>
 * <li>allowEditAnnotations</li>
 * <li>allowPrint</li>
 * <li>userPassword</li>
 * <li>ownerPassword</li></ul>
 * Specifying any of the above in {@link RenderDocumentArg#getRenderOptionsArg()} will result in the 
 * option being enabled. For example, the following will enable allowCopyContent and allowPrint options
 * (setting them to "TRUE" in the Apache FOP):<br/><br/>
 * <code>
 * <i>command</i>.setRenderOptionsArg("allowCopyContent=TRUE,allowPrint=TRUE,allowEditContent=FALSE");
 * </code><br/><br/>
 * Options not specified are set to FALSE by default.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class RenderPdfDocumentService extends Service {
    private RenderDocumentArg args = null;
    private Core core = null;

    /** Default constructor */
    public RenderPdfDocumentService() {
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
        v.setAuthor("$Author$");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date$");
        v.setSource("$Source$");
        v.setState("$State$");
        v.setVersion("$Revision$");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (RenderDocumentArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of GenerateDocumentArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    @SuppressWarnings("unchecked")
    public void invoke() throws PreconditionException, PostconditionException, RenderException {
        try {
            // The resulting PDF will end up in this array
            ByteArrayOutputStream output=new ByteArrayOutputStream();
            
            FopFactory fopFactory=FopFactory.newInstance();
            
            FOUserAgent agent=fopFactory.newFOUserAgent();
    
            // Copy the render options from config into the renderer
            for(String option: args.getRenderOptionsArg().split(",")) {            
                agent.getRendererOptions().put(option.split("=")[0], option.split("=")[1]);
            }
    
            agent.setProducer("Applied Industrial Logic");
    
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, agent, output);
                    
            URL xsltUrl = new URL(args.getTranslationUrlArg());
    
            // Hard code the use of saxon - at least until xalan supports XSLT 2.0
            TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
    
            Transformer transformer = factory.newTransformer(new StreamSource(xsltUrl.openStream()));
        
            // Setup input for XSL:FO transformation
            Source src = new StreamSource(new StringReader(args.getSourceDataArg().toStringWithEntityReferences(true)));
            
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            
            // Start XSL:FO transformation and FOP processing
            transformer.transform(src, res);
            
            encrypt(output);
            
            args.setRenderedDocumentRet(output.toByteArray());
        }
        catch (MalformedURLException e1) {
            throw new RenderException("The specified translation URL ("+args.getTranslationUrlArg()+") is malformed");
        }
        catch (IOException e) {
            throw new RenderException("Failed to read translation from URL ("+args.getTranslationUrlArg()+"):"+e);
        }
        catch(TransformerException e) {
            e.printStackTrace();
            throw new RenderException("Translation exception: "+e);
        }
        catch (DocumentException e) {
            throw new RenderException("PDF encryption failed: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RenderException("PDF generation failed: " + e);
        }
    }

    private void encrypt(ByteArrayOutputStream pdf) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(pdf.toByteArray());
        Document document = new Document(reader.getPageSizeWithRotation(1));
        PdfWriter writer = PdfWriter.getInstance(document, pdf);
        writer.setEncryption(PdfWriter.STRENGTH128BITS, "pdf", null, PdfWriter.AllowCopy);
    }
}

