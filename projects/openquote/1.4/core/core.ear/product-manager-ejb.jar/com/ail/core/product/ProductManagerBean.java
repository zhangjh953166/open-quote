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

package com.ail.core.product;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.EJBComponent;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.product.ListProductsService.ListProductsArgument;
import com.ail.core.product.NewProductTypeService.NewProductTypeArgument;
import com.ail.core.product.RegisterProductService.RegisterProductArgument;
import com.ail.core.product.RemoveProductService.RemoveProductArgument;
import com.ail.core.product.ResetAllProductsService.ResetAllProductsArgument;
import com.ail.core.product.ResetProductService.ResetProductArgument;
import com.ail.core.product.UpdateProductService.UpdateProductArgument;

@Configurable
public class ProductManagerBean extends EJBComponent implements SessionBean {
    private VersionEffectiveDate versionEffectiveDate = null;
    private Core core = null;
    private SessionContext ctx = null;

    public ProductManagerBean() {
        versionEffectiveDate = new com.ail.core.VersionEffectiveDate();
        core = new Core(this);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void ejbCreate() throws CreateException {
        versionEffectiveDate = new com.ail.core.VersionEffectiveDate();
        core = new com.ail.core.Core(this);
    }

    /**
     * Expose services of this EJB via XML. This method unmarshals the XML argument string into
     * an object, finds a method on the EJB to accept that object type as an argument
     * and invokes it. The result returned from the method is marshalled back into XM and returned.<p>
     * The methods are invoked on the context's local interface if possible (if one
     * exists). If no local interface is found then the remote interface is used instead.
     * Invoking methods via the local/remote interface means that the deployment setting
     * for security and transacts will be honoured.
     * @param xml XML argument to be passed to the service.
     * @return XML returned from the service.
     */
    public String invokeServiceXML(String xml) {
        return super.invokeServiceXML(xml, ctx);
    }

    public Core getCore() {
        return core;
    }

    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    public void setConfiguration(Configuration config) {
        try {
            super.setConfiguration(config);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public Configuration getConfiguration() {
        try {
            return super.getConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public String getConfigurationNamespace() {
        return super.getConfigurationNamespace();
    }

    public void resetConfiguration() {
        try {
            super.resetConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    /**
     * Service wrapper method for the ListProducts service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ListProductsArgument getListProducts(ListProductsArgument arg) throws BaseServerException {
        return invokeCommand(core, "ListProducts", arg);
    }

    /**
     * Service wrapper method for the RegisterProduct service.
     * @param arg Command to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public RegisterProductArgument registerProduct(RegisterProductArgument arg) throws BaseServerException {
        return invokeCommand(core, "RegisterProduct", arg);
    }

    /**
     * Service wrapper method for the RemoveProduct service.
     * @param arg Command to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public RemoveProductArgument removeProduct(RemoveProductArgument arg) throws BaseServerException {
        return invokeCommand(core, "RemoveProduct", arg);
    }

    /**
     * Service wrapper method for the ResetProduct service.
     * @param arg Command to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ResetProductArgument getProductDefinition(ResetProductArgument arg) throws BaseServerException {
        return invokeCommand(core, "ResetProduct", arg);
    }

    /**
     * Service wrapper method for the ResetAllProductsCommand service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ResetAllProductsArgument resetAllProducts(ResetAllProductsArgument arg) throws BaseServerException {
        return invokeCommand(core, "ResetAllProducts", arg);
    }

    /**
     * Service wrapper method for the UpdateProductDefinition service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public UpdateProductArgument updateProduct(UpdateProductArgument arg) throws BaseServerException {
        return invokeCommand(core, "UpdateProduct", arg);
    }

    /**
     * Service wrapper method for the NewProductType service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public NewProductTypeArgument newProductType(NewProductTypeArgument arg) throws BaseServerException {
        return invokeCommand(core, "NewProductType", arg);
    }
}


