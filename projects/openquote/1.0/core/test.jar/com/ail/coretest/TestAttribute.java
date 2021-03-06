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

package com.ail.coretest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.Attribute;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source: /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestTypeXpath.java,v $
 */
public class TestAttribute extends TestCase {

    /** Constructs a test case with the given name. */
    public TestAttribute(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestAttribute.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public void testAttributeValidation() {
        Attribute attr;

        attr = new Attribute("q1", "1", "number;min=0;max=21");
        attr.setValue("-1");
        assertTrue(attr.isInvalid());
        attr.setValue("22");
        assertTrue(attr.isInvalid());
        attr.setValue("10");
        assertTrue(!attr.isInvalid());

        attr = new Attribute("q1", "1", "currency;min=0.20;max=21", "GBP");
        attr.setValue("0.12");
        assertTrue(attr.isInvalid());
        attr.setValue("22.54");
        assertTrue(attr.isInvalid());
        attr.setValue("10");
        assertTrue(!attr.isInvalid());

        attr = new Attribute("q1", "hello", "string;min=4;max=20");
        attr.setValue("ooo");
        assertTrue(attr.isInvalid());
        attr.setValue("ooooooooooooooooooooo");
        assertTrue(attr.isInvalid());
        attr.setValue("ooooooooooooo");
        assertTrue(!attr.isInvalid());

        attr = new Attribute("q1", "hello", "string;min=0;max=20");
        attr.setValue("");
        assertTrue(!attr.isInvalid());
        attr.setValue("oooooooooo");
        assertTrue(!attr.isInvalid());
        attr.setValue("ooooooooooooooooooooo");
        assertTrue(attr.isInvalid());

        attr = new Attribute("q1", "hello", "string;pattern=[0-9a-z]*");
        attr.setValue("abc123");
        assertTrue(!attr.isInvalid());
        attr.setValue("Aabc123");
        assertTrue(attr.isInvalid());

        attr = new Attribute("q1", "30/11/1978", "date;pattern=dd/MM/yyyy");
        attr.getObject();
    }

    public void testAttributeFormatting() {
        Attribute attr;

        attr = new Attribute("amount5", "�12.234", "number;pattern=�#.##");
        assertEquals("�12.23", attr.getFormattedValue());
    }

    public void testReference() {
        Attribute attr;

        Attribute.setReferenceContext(new Attribute("refdata", "?", "choice,options=-1#?|1#Salon|2#Coupe|3#Convertible"));

        attr = new Attribute("id123", "?", "ref=/format");
        assertFalse(attr.isInvalid());

        attr.setValue("Salon");
        assertFalse(attr.isInvalid());

        attr.setValue("Hello");
        assertTrue(attr.isInvalid());
    }

    /**
     * Test that commas in a format option list are seen as part of the option text itself, and not a format separator.
     */
    public void testChoiceOptionWithCommas() {
        Attribute a = new Attribute("refdata", "?", "choice,options=-1#?|1#Salon|2#House, Boat|3#Convertible, ship");
        assertEquals("-1#?|1#Salon|2#House, Boat|3#Convertible, ship", a.getFormatOption("options"));
    }

    /**
     * Test choice attribute formatting
     */
    public void testChoiceFormatting() {
        Attribute a = new Attribute("refdata", "Convertible, ship", "choice,options=-1#?|1#Salon|2#House, Boat|3#Convertible, ship");
        assertEquals("Convertible, ship", a.getFormattedValue());
        assertEquals("Convertible, ship", a.getValue());
        assertEquals(3.0, a.getObject());

    }

    public void testCurrencyAttribute() {
        Attribute attr = new Attribute("q1", "1002.23", "currency", "GBP");

        assertEquals(java.lang.String.class, attr.getValue().getClass());
        assertEquals(java.lang.Double.class, attr.getObject().getClass());

        assertEquals("1,002.23 GBP", attr.getFormattedValue());
        assertEquals("1002.23", attr.getValue());
        assertEquals(1002.23, attr.getObject());
        System.out.print(attr.getObject().getClass().getName());
    }
}
