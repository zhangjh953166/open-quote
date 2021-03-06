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

package com.ail.core.xmlbinding;

import static org.junit.Assert.*;

import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.core.configure.Builder;
import com.ail.core.configure.Builders;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.Type;
import com.ail.core.configure.Types;

/**
 * The tests defined here exercise the Core system's factory. They use the Core
 * class as a Service or core client would. Note: These tests assume that the
 * JDBCConfigurationLoader is being used.
 */
public class TestCoreXMLBinding implements CoreUser, ConfigurationOwner {
    private AbstractConfigurationLoader loader = null;
    private Core core = null;
    private VersionEffectiveDate versionEffectiveDate = null;
    private String TestNamespace = "TESTNAMESPACE";
    private String configurationNamespace = TestNamespace;
    private String simpleTestString = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
        "<version systemId=\"-1\" attributeCount=\"0\" lock=\"false\" persisted=\"false\" serialVersion=\"0\" xsi:type=\"java:com.ail.core.Version\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"+
        " <version>1.0</version>\n"+
        " <date>14/10/2002</date>\n"+
        " <copyright>Copyright us.</copyright>\n"+
        " <author>T.S.Elliot</author>\n"+
        " <source>Peach and mint</source>\n"+
        " <state>state</state>\n"+
        " <comment>The loganberry's are sweet</comment>\n"+
        "</version>\n";

    /**
     * Tidy up (delete) the testdata generated by this set of tests.
     */
    private void tidyUpTestData() {
        // load the loader
        if (loader == null) {
            loader = AbstractConfigurationLoader.loadLoader();
        }

        // fetch the loader's properties
        Properties props = loader.getLoaderParams();

        // load the JDBC driver
        try {
            Class.forName(props.getProperty("driver"));
            Connection con = DriverManager.getConnection(props.getProperty("url") + props.getProperty("databaseName"), props);
            Statement st = con.createStatement();
            st.execute("DELETE FROM " + props.getProperty("table") + " WHERE NAMESPACE='" + this.TestNamespace + "'");
            st.close();
            con.close();
        }
        catch (Exception e) {
            // output the error if it isn't a table unknown error.
            if (e.getMessage().indexOf("Unknown table")==0) {
                System.err.println("ignored: "+e);
            }
        }
    }

    /**
     * Create a simple configuration for use in tests. <Configuration
     * name="SimpleConfigurationName" version="9.1"> <Builders> <Builder
     * name="ClassBuilder" factory="com.ail.core.factory.ClassFactory"/>
     * </Builders> <Types> <Type name="ClassAccessor" builder="ClassBuilder"
     * key="com.ail.core.command.ClassAccessor"> <Parameter name="ServiceClass"
     * value="com.ail.core.logging.SystemOutLoggerService"/> </Type> <Type
     * name="InfoLogger" builder="ClassBuilder"
     * key="com.ail.core.logging.LoggerCommand"> <Parameter name="Accessor"
     * value="ClassAccessor"/> </Type> </Types> </Configuration>
     * 
     * @return An instance of a simple configuration
     */
    private Configuration createSimpleConfiguration() {
        Parameter p = null;
        Types ts = null;
        Type t = null;

        Configuration config = new Configuration();
        config.setName("SimpleConfigurationName");
        config.setVersion("9.1");

        Builders blds = new Builders();
        config.setBuilders(blds);

        Builder b = new Builder();
        blds.addBuilder(b);
        b.setName("ClassBuilder");
        b.setFactory("com.ail.core.factory.ClassFactory");

        ts = new Types();
        config.setTypes(ts);
        t = new Type();
        ts.addType(t);
        t.setName("ToXMLService");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.command.ClassAccessor");
        p = new Parameter();
        p.setName("ServiceClass");
        p.setValue("com.ail.core.xmlbinding.CastorToXMLService");
        t.addParameter(p);

        t = new Type();
        ts.addType(t);
        p = new Parameter();
        t.setName("com.ail.core.xmlbinding.ToXMLService.ToXMLCommand");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.xmlbinding.ToXMLCommandImpl");
        p.setName("Accessor");
        p.setValue("ToXMLService");
        t.addParameter(p);

        t = new Type();
        ts.addType(t);
        t.setName("FromXMLService");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.command.ClassAccessor");
        p = new Parameter();
        p.setName("ServiceClass");
        p.setValue("com.ail.core.xmlbinding.CastorFromXMLService");
        t.addParameter(p);

        t = new Type();
        ts.addType(t);
        p = new Parameter();
        t.setName("com.ail.core.xmlbinding.FromXMLService.FromXMLCommand");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.xmlbinding.FromXMLCommandImpl");
        p.setName("Accessor");
        p.setValue("FromXMLService");
        t.addParameter(p);

        t = new Type();
        ts.addType(t);
        t.setName("Version");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.Version");

        return config;
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and
     * delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser"); 
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        versionEffectiveDate = new VersionEffectiveDate();
        tidyUpTestData();
        core = new Core(this);
        core.resetConfiguration();
        Configuration config = createSimpleConfiguration();
        core.setConfiguration(config);
        versionEffectiveDate = new VersionEffectiveDate();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
        tidyUpTestData();
    }

    /**
     * Method demanded by the CoreUser interface.
     * 
     * @return A date to use to select the current version of config info.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        return null;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * 
     * @param config
     *            Configuration to use from now on.
     */
    public void setConfiguration(Configuration config) {
        core.setConfiguration(config);
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * 
     * @return The current configuration (at versionEffectiveDate).
     */
    public Configuration getConfiguration() {
        return core.getConfiguration();
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * 
     * @return The configuration namespace we're using
     */
    public String getConfigurationNamespace() {
        return configurationNamespace;
    }

    public void setConfigurationNamespace(String configurationNamespace) {
        this.configurationNamespace = configurationNamespace;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     */
    public void resetConfiguration() {
    }

    /**
     * This text checks that XMLBinding correctly converts an object into an
     * XMLString.
     * <ol>
     * <li>Create an instance of 'Version', and populate it with values.</li>
     * <li>Convert the version object into XML using the core.</li>
     * <li>Check the resulting XML against a hard coded result.</li>
     * <li>Fail if the strings do not match.</li>
     * <li>Fail if any exceptions are thrown.
     * <li>
     * </ol>
     */
    @Test
    public void testSimpleToXML() {
        try {
            Version version = (Version) core.newType("Version");
            version.setState("state");
            version.setVersion("1.0");
            version.setAuthor("T.S.Elliot");
            version.setComment("The loganberry's are sweet");
            version.setCopyright("Copyright us.");
            version.setDate("14/10/2002");
            version.setSource("Peach and mint");
            XMLString xml = core.toXML(version);
            assertEquals(simpleTestString, xml.toString());
        }
        catch (Throwable e) {
            fail("Unexpected exception:" + e);
        }
    }

    /**
     * Check that the core corrently converts a string of XML into the
     * corresponding object.
     */
    @Test
    public void testSimpleFromXML() {
        try {
            Version version = core.fromXML(Version.class, new XMLString(simpleTestString));
            assertEquals("state", version.getState());
            assertEquals("1.0", version.getVersion());
            assertEquals("T.S.Elliot", version.getAuthor());
            assertEquals("The loganberry's are sweet", version.getComment());
            assertEquals("Copyright us.", version.getCopyright());
            assertEquals("14/10/2002", version.getDate());
            assertEquals("Peach and mint", version.getSource());
        }
        catch (Throwable e) {
            fail("Unexpected exception:" + e);
        }
    }

    /**
     * The XMLString class offers some help in determining the java type
     * represented by an XML string - assuming that string has an xsi:type
     * attribute in the root element. These tests check that the detection is
     * working correctly.
     */
    @Test
    public void testXsiTypeDetection() throws Exception {
        XMLString test;

        test = new XMLString("<version></version>");
        assertNull(test.getXsiType());

        try {
            test.getType();
            fail("found a type when no xsi:type was present");
        }
        catch (ClassNotFoundException e) {
            // ignore this - its good
        }

        test = new XMLString("<version xsi:type='java:com.ail.core.Version'></version>");
        assertEquals("com.ail.core.Version", test.getXsiType());
        assertTrue(Version.class == test.getType());

        test = new XMLString("<version xsi:type=\"java:com.ail.core.Version\"></version>");
        assertEquals("com.ail.core.Version", test.getXsiType());
        assertTrue(Version.class == test.getType());

        test = new XMLString("<?xml version=\"1.0\" encoding=\"UTF-8\"?><version xsi:type='java:com.ail.core.Version'></version>");
        assertEquals("com.ail.core.Version", test.getXsiType());
        assertTrue(Version.class == test.getType());

        test = new XMLString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<version xsi:type='java:com.ail.core.Version'></version>");
        assertEquals("com.ail.core.Version", test.getXsiType());
        assertTrue(Version.class == test.getType());
    }

    /**
     * Test how stacked attributes are interpreted and that xpath works on them.
     * The Type class has the notion of attributes - i.e. a collection of name
     * value pairs what may itself contain other sub-collections. This test
     * checks that these nested attribute collections can be converted from
     * String to type, and that the type can be XPath'ed.
     * <ol>
     * <li>Create an XML string representing a type (Version will do)
     * containing attributes</li>
     * <li>Use the core's fromXML method to translate the string into an
     * instance of Version</li>
     * <li>Use XPath queries to return attribute value and check them to be
     * correct</li>
     * <li>Fail if any exceptions are thrown</li>
     * <li>Fail if any of the value are not correct</li>
     * </ol>
     * 
     * @throws Exception
     */
    @Test
    public void testAttributeInAttributeXpathTest() throws Exception {
        String versionString = "<version>" + "<attribute id='car'>" + "<attribute id='colour' value='black' format='string'/>"
                + "<attribute id='enginesize' value='2000' format='number,integer' unit='cc'/>"
                + "<attribute id='make' value='ford' format='string'/>" + "<attribute id='model' value='escort' format='string'/>"
                + "</attribute>" + "</version>";

        Version version = core.fromXML(Version.class, new XMLString(versionString));
        assertEquals("black", version.xpathGet("attribute[id='car']/attribute[id='colour']/value"));
        assertEquals("ford", version.xpathGet("attribute[id='car']/attribute[id='make']/object"));
    }
}
