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

package com.ail.core.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.Type;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandInvocationError;
import com.ail.core.factory.RecursiveTypeError;
import com.ail.core.factory.UndefinedTypeError;
import com.ail.core.key.GenerateUniqueKeyService.GenerateUniqueKeyCommand;

public class TestProductServices extends CoreUserBaseCase {

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and
     * delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        tidyUpTestData();
        setCore(new Core(this));
        resetConfigurations();
    }

    /**
     * Always select the latest configurations
     * 
     * @return
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
        tidyUpTestData();
    }

    /**
     * The core class exposes the services offered by the core via easy to use
     * interfaces. This test checks that those interfaces are present and throw
     * the appropriate exceptions when bad arguments are passed into the
     * services.
     * <ul>
     * <li>Invoke core.newProductType() with a null argument</li>
     * <li>Fail if a CommandInvocationError isn't thrown (null is an illegal
     * argument).</li>
     * <li>Invoke core.resetProduct() with a null argument</li>
     * <li>Fail if a CommandInvocationError isn't thrown (null is an illegal
     * argument).</li>
     * <li>Invoke core.resetAllProducts()</li>
     * <li>Fail if any exceptions are thrown (resetAll doesn't take any args).</li>
     * <li>Invoke core.listProducts()</li>
     * <li>Fail if any exceptions are thrown (listProducts doesn't take any
     * args).</li>
     * </ul>
     */
    @Test
    public void testServicesViaCore() {
        try {
            getCore().newProductType(null);
            fail("newProductType shouldn't accept null as an argument");
        } catch (CommandInvocationError e) {
            // this is what we want
        }

        try {
            getCore().resetProduct(null);
            fail("resetProduct shouldn't accept null as an argument");
        } catch (CommandInvocationError e) {
            // this is what we want
        }

        try {
            getCore().resetAllProducts();
        } catch (CommandInvocationError e) {
            e.printStackTrace();
            fail("Unexpeceted exception: " + e);
        }

        try {
            getCore().listProducts();
        } catch (CommandInvocationError e) {
            e.printStackTrace();
            fail("Unexpeceted exception: " + e);
        }
    }

    @Test
    public void testProductResetService() {
        getCore().resetProduct("com.ail.core.product.TestProduct01");
        getCore().resetProduct("com.ail.core.product.TestProduct02");
        getCore().resetProduct("com.ail.core.product.TestProduct03");

        try {
            getCore().resetProduct("product.that.does.not.exist");
            fail("managed to reset a product that doesn't exist!");
        } catch (CommandInvocationError e) {
            // A "Product not found" is what we want to see.
            if (!e.getDescription().startsWith("Product not found")) {
                fail("Unexpected exception: " + e);
            }
        }
    }

    /**
     * The ListProducts service returns a list of all the products known to the
     * system. It returns a Collection of instances of String, each representing
     * the name of a product. If no products are defined it returns an empty
     * Collections. The Core defines three test products, only these should be
     * returned by this service.
     * <ul>
     * <li>Invoke core.listProducts</li>
     * <li>Fail if any exceptions are thrown</li>
     * <li>Fail if the call returns a null</li>
     * <li>Check that the Core's two default test products, and only these two,
     * are in the list</li> </ol>
     */
    @Test
    public void testListProductsService() {
        Collection<ProductDetails> prods = getCore().listProducts();

        assertNotNull(prods);

        List<String> pds = new ArrayList<String>();
        for (ProductDetails dets : prods) {
            pds.add(dets.getName());
        }

        assertEquals(12, pds.size());
        assertTrue(pds.contains("com.ail.core.product.TestProduct01"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct02"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct03"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct04"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct05"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct06"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct07"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct08"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct09"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct10"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct11"));
        assertTrue(pds.contains("com.ail.core.product.TestProduct12"));
    }

    /**
     * Each product definition must contain a "default" type. This test checks
     * that each of the test products does, and that they can be instantiated.
     */
    @Test
    public void testDefaultTypeInstantiation() {
        assertNotNull(getCore().newProductType(
                "com.ail.core.product.TestProduct01"));
        assertNotNull(getCore().newProductType(
                "com.ail.core.product.TestProduct02"));
        assertNotNull(getCore().newProductType(
                "com.ail.core.product.TestProduct03"));

        try {
            getCore().newProductType("product.that.does.not.exist");
            fail("Instantiated the default type for a product that doesn't exit!");
        } catch (UndefinedTypeError e) {
            // this is what we want!
        }
    }

    /**
     * Each product can define any number of named types. This test checks that
     * the default product's named types are available, and that requests to
     * instantiate types that are not defined are handled correctly.
     */
    @Test
    public void testNonDefaultProductTypeInstantiation() {
        assertNotNull(getCore().newProductType(
                "com.ail.core.product.TestProduct01", "TestTypeA"));
        assertNotNull(getCore().newProductType(
                "com.ail.core.product.TestProduct02", "TestTypeB"));
        assertNotNull(getCore().newProductType(
                "com.ail.core.product.TestProduct03", "TestTypeB"));

        try {
            getCore().newProductType("com.ail.core.product.TestProduct03",
                    "TypeThatDoesNotExist");
            fail("Instantiated the default type for a product that doesn't exit!");
        } catch (UndefinedTypeError e) {
            // this is what we want!
        }

        try {
            getCore().newProductType("product.that.does.not.exist",
                    "TypeThatDoesNotExist");
            fail("Instantiated the default type for a product that doesn't exit!");
        } catch (UndefinedTypeError e) {
            // this is what we want!
        }
    }

    /**
     * A product definition that extends another may base its types on its
     * parent's types. This extends to the product default type. This test
     * checks that TestProduct3's default type does correctly inherit its
     * parent's settings, and also that it overrides some of them.
     * <p>
     * TestProduct2 defines four attributes and values:
     * <ol>
     * <li>name='TestProduct2'</li>
     * <li>productname='TestProduct2'</li>
     * <li>age='34'</li>
     * <li>colour='purple'</li>
     * </ol>
     * TestProduct3 extends TestProduct2, and overrides the following:
     * <ol>
     * <li>name='TestProduct3'</li>
     * <li>productname='TestProduct3'</li>
     * </ol>
     * The 'age' and 'colour' attributes inherited from TestProduct2.
     */
    @Test
    public void testProductDefaultTypeInheritance() {
        Type def = (Type) getCore().newProductType(
                "com.ail.core.product.TestProduct03");

        assertEquals("TestProduct3", def.xpathGet("attribute[id='name']/value"));
        assertEquals("TestProduct3",
                def.xpathGet("attribute[id='productname']/value"));

        assertEquals("34", def.xpathGet("attribute[id='age']/value"));
        assertEquals("purple", def.xpathGet("attribute[id='colour']/value"));
    }

    @Test
    public void testRegisterUpdateRemoveHappyPath() {
        try {
            getCore().registerProduct(
                    new ProductDetails("MyTestProduct", "My Test Product"));
            getCore().updateProduct("MyTestProduct",
                    new ProductDetails("MyOtherProduct", "Other Description"));
            getCore().removeProduct(
                    new ProductDetails("MyOtherProduct", "Other Description"));
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            fail("Unexpected throwable: ");
        }
    }

    /**
     * When Configurations are "reset" the URL of they are loaded from is put
     * into their source property. This has to present in order for resources to
     * be loaded using relative URLs - which is a common practice in products.
     * This test checks that the URL has been added correctly.
     */
    @Test
    public void testConfigurationSource() {
        // We can't check the whole path because it'll change from machine to
        // machine depending
        // on where the test source has been checked-out to. We'll make do with
        // checking just
        // the start and the end.
        assertTrue(getCore().getConfiguration().getSource().startsWith("file:"));
        assertTrue(getCore()
                .getConfiguration()
                .getSource()
                .endsWith(
                        "target/test/integration-test.jar/com/ail/core/product/TestProductServicesDefaultConfig.xml"));
    }

    /**
     * Test that an extending product type (i.e. one which extends another type)
     * handles extension correctly in the following circumstances:
     * <ol>
     * <li>When it extends a type of the same name, and a type matching that
     * name is defined by the parent product. This should operate exactly as a
     * normal "extends" would if the names were different.</li>
     * <li>When it extends a type of the same name, and that type is not defined
     * in the parent product. A type cannot extend itself, so an exception
     * should be thrown when this situation is detected.</li>
     * <li>When it extends a type which is undefined. Again, an exception should
     * be thrown.</li>
     * </ol>
     * 
     * @throws Exception
     */
    @Test
    public void testDuplicateTypeNameHandling() throws Exception {
        Type t = null;

        t = getCore().newProductType("com.ail.core.product.TestProduct03",
                "TestTypeC");
        assertEquals("TestProduct2", t.xpathGet("attribute[id='source']/value"));
        assertEquals("TestTypeC", t.xpathGet("attribute[id='name']/value"));

        try {
            getCore().newProductType("com.ail.core.product.TestProduct03",
                    "TestTypeD");
            fail("Should have got an UndefinedTypeError");
        } catch (UndefinedTypeError e) {
            // ignore this - its what we want
        }

        try {
            getCore().newProductType("com.ail.core.product.TestProduct03",
                    "TestTypeE");
            fail("Should have got an RecursiveTypeError");
        } catch (RecursiveTypeError e) {
            // ignore this - it's what we want
        }
    }

    /**
     * Each product can maintain it's own values for the unique key generator.
     * When a product does not maintain it's own, the values are used from the
     * parent product or from the core. This test checks that products do
     * correctly maintain isolation between the values of unique keys.
     * 
     * @throws Exception
     */
    @Test
    public void testUniqueKeyGeneratorByProduct() throws Exception {
        GenerateUniqueKeyCommand gukc;

        gukc = getCore().newCommand(GenerateUniqueKeyCommand.class);
        gukc.setKeyIdArg("Key");
        gukc.setProductTypeIdArg("com.ail.core.product.TestProduct03");
        gukc.invoke();
        assertEquals(new Integer(1000), gukc.getKeyRet());

        gukc = getCore().newCommand(GenerateUniqueKeyCommand.class);
        gukc.setKeyIdArg("Key");
        gukc.setProductTypeIdArg("com.ail.core.product.TestProduct02");
        gukc.invoke();
        assertEquals(new Integer(100), gukc.getKeyRet());

        for (int i = 1001; i < 1100; i++) {
            gukc = getCore().newCommand(GenerateUniqueKeyCommand.class);
            gukc.setKeyIdArg("Key");
            gukc.setProductTypeIdArg("com.ail.core.product.TestProduct03");
            gukc.invoke();
            assertEquals(new Integer(i), gukc.getKeyRet());
        }

        for (int i = 10; i < 200; i++) {
            gukc = getCore().newCommand(GenerateUniqueKeyCommand.class);
            gukc.setKeyIdArg("Key");
            gukc.setProductTypeIdArg("com.ail.core.product.TestProduct01");
            gukc.invoke();
            assertEquals(new Integer(i), gukc.getKeyRet());
        }

        gukc = getCore().newCommand(GenerateUniqueKeyCommand.class);
        gukc.setKeyIdArg("Key");
        gukc.setProductTypeIdArg("com.ail.core.product.TestProduct02");
        gukc.invoke();
        assertEquals(new Integer(101), gukc.getKeyRet());
    }
}
