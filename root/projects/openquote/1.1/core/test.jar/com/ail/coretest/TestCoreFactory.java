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

package com.ail.coretest;

import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.core.configure.Builder;
import com.ail.core.configure.Builders;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.JDBCConfigurationLoader;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.Type;
import com.ail.core.configure.Types;
import com.ail.core.factory.FactoryConfigurationError;
import com.ail.core.factory.UndefinedTypeError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.security.Principal;
import java.sql.Connection;
import java.sql.Statement;

/**
 * The tests defined here exercise the Core system's factory.
 * They use the Core class as a Service or core client would.
 * Note: These tests assume that the JDBCConfigurationLoader is being used.
 * @version $Revision: 1.12 $
 * @state $State: Exp $
 * @date $Date: 2006/09/11 20:13:04 $
 * @source $Source: /home/bob/CVSRepository/projects/core/test.jar/com/ail/coretest/TestCoreFactory.java,v $
 */
public class TestCoreFactory extends TestCase implements CoreUser, ConfigurationOwner {
    private Core core=null;
    private VersionEffectiveDate versionEffectiveDate=null;
    private String TestNamespace="TESTNAMESPACE";
    private String configurationNamespace=TestNamespace;

    public TestCoreFactory() {
        this("TestCoreFactory");
        core=new Core(this);
    }

    /**
     * Constructs a test case with the given name.
     */
    public TestCoreFactory(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestCoreFactory.class);
    }

    public static void main (String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Tidy up (delete) the testdata generated by this set of tests.
     */
    private void tidyUpTestData() {
        try {
            JDBCConfigurationLoader jload=(JDBCConfigurationLoader)AbstractConfigurationLoader.loadLoader();
            Connection con=jload.openConnection();
            Statement st=con.createStatement();
            st.execute("DELETE FROM config WHERE NAMESPACE='"+this.TestNamespace+"'");
            st.execute("DELETE FROM config WHERE NAMESPACE='"+Core.CoreNamespace+"'");
            st.close();
            con.close();
        }
        catch(Exception e) {
          e.printStackTrace();
          fail("failed to clear testnamespace from table");
        }
    }

    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        return null;
    }

    /**
     * Save a known core configuration.
     * This configuration has only one parameter: TestParameter. The config.
     * is used in testing to ensure that the core's configuration is used
     * as a backstop when the local configuration doesn't define a setting.
     */
    private void writeCoreConfiguration() {
        Configuration config=new Configuration();
        config.setName(Core.CoreNamespace);
        config.setVersion("0.0");
        setConfigurationNamespace(Core.CoreNamespace);
        setConfiguration(config);
        setConfigurationNamespace(TestNamespace);
    }

    /**
     * Create a complex configuration object in the following form:
     * Configuration
     *    <?xml version="1.0" encoding="UTF-8"?>
     *
     *    <Configuration version="1.1" timeout="5" >
     *      <Builders>
     *         <Builder name="classBuilder" factory="com.ail.core.factory.ClassFactory"/>
     *      </Builders>
     *
     *        <Types>
     *         <Type name="Version" builder="classBuilder" key="com.ail.core.Version"/>
     *         <Type name="SetVersion" builder="classBuilder" key="com.ail.core.Version"/>
     *            <Parameter name="version" value="1.2"/>
     *            <Parameter name="author" value="Fred"/>
     *         </Type>
     *        </Types>
     *    </Configuration>
     */
    private Configuration createConfigurationOne() {
        Configuration config;
        Parameter p;
        Types ts;
        Type t;
        Builders blds;
        Builder b;
        
        config=new Configuration();
        config.setVersion("1.1");
        config.setTimeout(5);
        
        blds=new Builders();
        config.setBuilders(blds);
        
        b=new Builder();
        blds.addBuilder(b);
        b.setName("classBuilder");
        b.setFactory("com.ail.core.factory.ClassFactory");
        blds.addBuilder(b);
        
        b=new Builder();
        blds.addBuilder(b);
        b.setName("BeanShellBuilder");
        b.setFactory("com.ail.core.factory.BeanShellFactory");
        blds.addBuilder(b);
        
        b=new Builder();
        blds.addBuilder(b);
        b.setName("FromXMLBuilder");
        b.setFactory("com.ail.core.factory.CastorXMLFactory");
        blds.addBuilder(b);
        
        b=new Builder();
        blds.addBuilder(b);
        b.setName("FromXSLTBuilder");
        b.setFactory("com.ail.core.factory.XSLTFactory");
        blds.addBuilder(b);
        
        ts=new Types();
        config.setTypes(ts);
        
        t=new Type();
        ts.addType(t);
        t.setName("ToXMLService");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.command.ClassAccessor");
        p=new Parameter();
        p.setName("ServiceClass");
        p.setValue("com.ail.core.xmlbinding.CastorToXMLService");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        p=new Parameter();
        t.setName("ToXML");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.xmlbinding.ToXMLCommand");
        p.setName("Accessor");
        p.setValue("ToXMLService");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("FromXMLService");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.command.ClassAccessor");
        p=new Parameter();
        p.setName("ServiceClass");
        p.setValue("com.ail.core.xmlbinding.CastorFromXMLService");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        p=new Parameter();
        t.setName("FromXML");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.xmlbinding.FromXMLCommand");
        p.setName("Accessor");
        p.setValue("FromXMLService");
        t.addParameter(p);
        
        
        t=new Type();
        ts.addType(t);
        t.setName("Version");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.Version");
        
        t=new Type();
        ts.addType(t);
        t.setName("SetVersion");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Version");
        p.setValue("1.2");
        t.addParameter(p);
        p=new Parameter();
        p.setName("Author");
        p.setValue("Fred");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("BeanShellVersion");
        t.setBuilder("BeanShellBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Script");
        p.setValue("type=new com.ail.core.Version(); type.setAuthor(\"Jim\"); type.setVersion(\"1.3\");");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("BeanShellVersionByUrl");
        t.setBuilder("BeanShellBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Url");
        p.setValue("classpath://com.ail.coretest/TestCoreFactoryBeanShellVersion.bs");
        t.addParameter(p);

        t=new Type();
        ts.addType(t);
        t.setName("BeanShellVersionCoreRef");
        t.setBuilder("BeanShellBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Script");
        p.setValue("type=core.newType(\"BeanShellVersion\");");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("CastorXMLVersion");
        t.setBuilder("FromXMLBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Script");
        p.setValue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                   "<version serialVersion='0' lock='false' xsi:type='java:com.ail.core.Version' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>" +
                      "<source>Peach and mint</source>"+
                      "<state>state</state>"+
                      "<date>14/10/2002</date>"+
                      "<author>T.S.Elliot</author>"+
                      "<comment>The loganberry's are sweet</comment>"+
                      "<copyright>Copyright us.</copyright>"+
                      "<version>1.0</version>"+
                      "<attribute id='one' value='valueone' format='string'/>"+
                      "<attribute id='two' value='valuetwo' format='string'/>"+
                   "</version>");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("CastorXMLVersionByUrl");
        t.setBuilder("FromXMLBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Url");
        p.setValue("classpath://com.ail.coretest/TestCoreFactoryCastorVersion.xml");
        t.addParameter(p);

        t=new Type();
        ts.addType(t);
        t.setName("CastorXMLVersionSingleInstance");
        t.setBuilder("FromXMLBuilder");
        t.setKey("com.ail.core.Version");
        t.setSingleInstance(true);
        p=new Parameter();
        p.setName("Script");
        p.setValue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                   "<version serialVersion='0' lock='false' xsi:type='java:com.ail.core.Version' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>" +
                      "<source>Peach and mint</source>"+
                      "<state>state</state>"+
                      "<date>14/10/2002</date>"+
                      "<author>T.S.Elliot</author>"+
                      "<comment>The loganberry's are sweet</comment>"+
                      "<copyright>Copyright us.</copyright>"+
                      "<version>1.0</version>"+
                      "<attribute id='one' value='valueone' format='string'/>"+
                      "<attribute id='two' value='valuetwo' format='string'/>"+
                   "</version>");
        t.addParameter(p);

        t=new Type();
        ts.addType(t);
        t.setName("BadBeanShellVersion");
        t.setBuilder("BeanShellBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Script");
        p.setValue("type=new com.ail.core.Version() pqowie; type.setAuthor(\"Tom\"); type.setVersion(\"2.3\");");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("XSLTVersion");
        t.setBuilder("FromXSLTBuilder");
        t.setKey("com.ail.core.Version");
        
        p=new Parameter();
        p.setName("extends");
        p.setValue("CastorXMLVersion");
        t.addParameter(p);
        
        p=new Parameter();
        p.setName("script");
        p.setValue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                   "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\">"+
                     "<xsl:output encoding=\"UTF-8\" indent=\"no\" method=\"xml\" version=\"1.0\"/>"+
                     "<xsl:template match='/'>"+
                       "<xsl:apply-templates/>"+
                     "</xsl:template>"+
                     "<xsl:template match='@*|*'>"+
                       "<xsl:copy>"+
                         "<xsl:apply-templates select='@*|node()'/>"+
                       "</xsl:copy>"+
                     "</xsl:template>"+
                     "<xsl:template match='source/text()'>orange and pineapple</xsl:template>"+
                   "</xsl:stylesheet>");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("XSLTVersionNoExtends");
        t.setBuilder("FromXSLTBuilder");
        t.setKey("com.ail.core.Version");
        
        p=new Parameter();
        p.setName("script");
        p.setValue("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                   "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\">"+
                     "<xsl:output encoding=\"UTF-8\" indent=\"no\" method=\"xml\" version=\"1.0\"/>"+
                     "<xsl:template match='/'>"+
                       "<version serialVersion='0' lock='false' xsi:type='java:com.ail.core.Version' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>" +
                         "<source>Peach and mint</source>"+
                         "<state>state</state>"+
                         "<date>14/10/2002</date>"+
                         "<author>T.S.Elliot</author>"+
                         "<comment>The loganberry's are sweet</comment>"+
                         "<copyright>Copyright us.</copyright>"+
                         "<version>1.0</version>"+
                       "</version>"+
                     "</xsl:template>"+
                   "</xsl:stylesheet>");
        t.addParameter(p);
        
        t=new Type();
        ts.addType(t);
        t.setName("BeanShellExtendsVersion");
        t.setBuilder("BeanShellBuilder");
        t.setKey("com.ail.core.Version");
        
        p=new Parameter();
        p.setName("extends");
        p.setValue("CastorXMLVersion");
        t.addParameter(p);
        
        p=new Parameter();
        p.setName("script");
        p.setValue("type=new com.ail.core.Version();type.setSource(\"Peach & lemon\");");
        t.addParameter(p);
        
        return config;
    }

    /**
     * Create a complex configuration object in the following form:
     * (This is the same a ConfigutaionOne - but with a different SetVersion).
     *
     *    <Configuration version="1.1" timeout="5" >
     *      <Builders>
     *         <Builder name="classBuilder" factory="com.ail.core.factory.ClassFactory"/>
     *      </Builders>
     *
     *        <Types>
     *         <Type name="Version" builder="classBuilder" key="com.ail.core.Version"/>
     *         <Type name="SetVersion" builder="classBuilder" key="com.ail.core.Version"/>
     *            <Parameter name="version" value="1.3"/>
     *            <Parameter name="author" value="Harry"/>
     *         </Type>
     *        </Types>
     *    </Configuration>
     */
    private Configuration createConfigurationTwo() {
        Configuration config;
        Parameter p;
        Types ts;
        Type t;
        Builders blds;
        Builder b;

        config=new Configuration();
        config.setVersion("1.1");
        config.setTimeout(5);

        blds=new Builders();
        config.setBuilders(blds);

        b=new Builder();
        blds.addBuilder(b);
        b.setName("classBuilder");
        b.setFactory("com.ail.core.factory.ClassFactory");
        blds.addBuilder(b);

        ts=new Types();
        config.setTypes(ts);
        t=new Type();
        ts.addType(t);
        t.setName("Version");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.Version");

        t=new Type();
        ts.addType(t);
        t.setName("SetVersion");
        t.setBuilder("classBuilder");
        t.setKey("com.ail.core.Version");
        p=new Parameter();
        p.setName("Version");
        p.setValue("1.3");
        t.addParameter(p);
        p=new Parameter();
        p.setName("Author");
        p.setValue("Harry");
        t.addParameter(p);

        return config;
    }

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
        core=new Core(this);
        versionEffectiveDate=new VersionEffectiveDate();
        tidyUpTestData();
        writeCoreConfiguration();
        core=new Core(this);
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    protected void tearDown() {
        tidyUpTestData();
    }

    /**
     * Method demanded by the CoreUser interface.
     * @return A date to use to selecte the corrent version of config info.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @param config Configuration to use from now on.
     */
    public void setConfiguration(Configuration config) {
        core.setConfiguration(config);    
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @return The current configuration (at versionEffectiveDate).
     */
    public Configuration getConfiguration() {
        return core.getConfiguration();
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @return The configuration namespace we're using
     */
    public String getConfigurationNamespace() {
        return configurationNamespace;
    }

    public void setConfigurationNamespace(String configurationNamespace){
        this.configurationNamespace = configurationNamespace;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     */
    public void resetConfiguration() {
        setConfiguration(createConfigurationOne());
    }

    /**
     * Check that the type factory returns instances of a type as
     * configured. The configuration created but <code>createConfigurationOne</code>
     * defines a type 'Version' to be an instance of <code>com.ail.core.Version</code>.
     * This test ensures that an instance of <code>com.ail.core.Version</code> is returned
     * when 'Version' is requested.
     * <ol>
     * <li>Create a configuration using </code>createConfigurationOne</code></li>
     * <li>Use the type factory to create a 'Version' object.</li>
     * <li>Fail if the object returned is not an instance of <code>com.ail.core.Version</code></li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testSimpleType() {
        try {
            core.setConfiguration(this.createConfigurationOne());
            ConfigurationHandler.reset();
            versionEffectiveDate=new VersionEffectiveDate();
            Object o=core.newType("Version");
            assertTrue(o instanceof com.ail.core.Version);
        }
        catch(Throwable e) {
            fail("Unexpected exception:"+e);
        }
    }

    /**
     * Check that the type factory correctly handles a request for a type
     * that is not defined. If asked to create a type that is not defined
     * the factory should throw a ...
     * <ol>
     * <li>Create a configuration using </code>createConfigurationOne</code></li>
     * <li>Use the type factory to create a 'Type-That-Does-Not-Exist' object.</li>
     * <li>Fail if any object is returned</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testUndefinedSimpleType() {
        try {
            core.setConfiguration(this.createConfigurationOne());
            versionEffectiveDate=new VersionEffectiveDate();
            core.newType("Type-That-Does-Not-Exist");
            fail("Undefined was returned!");
        }
        catch(UndefinedTypeError e) {
            // this is what we're expecting.
        }
        catch(Throwable e) {
            fail("Unexpected exception:"+e);
        }
    }

    /**
     * Check that the type factory creates 'prototyped' types correctly.
     * Prototyped types are types that have some or all of their properties
     * defined in the configuration.
     * <ol>
     * <li>Create an instance of the 'SetVersion' type.</li>
     * <li>Fail if the returned object is not an instance of 'Version'.</li>
     * <li>Fail if the returned version's 'author' property is not set to "Fred".</li>
     * <li>Fail if the returned version's 'version' property is not set to "1.2".</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testSimplePrototypeType() {
        try {
            core.setConfiguration(this.createConfigurationOne());
            versionEffectiveDate=new VersionEffectiveDate();
            Object o=core.newType("SetVersion");
            assertTrue(o instanceof Version);
            Version v=(Version)o;
            assertEquals("Fred", v.getAuthor());
            assertEquals("1.2", v.getVersion());
        }
        catch(Throwable e) {
            fail("Unexpected exception:"+e);
        }
    }

    /**
     * Configuration history should maintain types by date. This means that is
     * should be possible to have two types by the same name active on different
     * effectiveDates. These types may also have different initial settings. This
     * test ensures that this is the case.<p>
     * TestCoreFactory defines two configurations, both define a type called
     * SetVersion, both define SetVersion with different initial settings. This
     * test saves both configurations in different effective dates, then by
     * shifting the effective date between the two settings we should get the
     * two different SetVersions back from the factory.
     * <ul>
     * <li>Save configuration one.</li>
     * <li>Set the effective date to now.</li>
     * <li>set timeOne to the effective date.</li>
     * <li>Sleep for 10ms.</li>
     * <li>Save configuration two.</li>
     * <li>Set the effective date to now.</li>
     * <li>set timeTwo to the effective date.</li>
     * <li>Sleep for 10ms.</li>
     * <li>Set the effective date to timeOne.</li>
     * <li>Create an instance of 'SetVersion' using the factory.</li>
     * <li>Fail if the object returns does not have the settings defined in configuration one.</li>
     * <li>Set the effective date to timeTwo.</li>
     * <li>Create an instance of 'SetVersion' using the factory.</li>
     * <li>Fail if the object returns does not have the settings defined in configuration two.</li>
     * <li>Repeat the last six steps once.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ul>
     */
    public void testDoubleVersionPrototypeType() {
        Version v=null;
        try {
            core.setConfiguration(this.createConfigurationOne());
            versionEffectiveDate=new VersionEffectiveDate();
            VersionEffectiveDate timeOne=versionEffectiveDate;
            Thread.sleep(10);

            core.setConfiguration(this.createConfigurationTwo());
            versionEffectiveDate=new VersionEffectiveDate();
            VersionEffectiveDate timeTwo=versionEffectiveDate;
            Thread.sleep(10);

            for(int i=0 ; i<2 ; i++) {
                versionEffectiveDate=timeOne;
                v=(Version)core.newType("SetVersion");
                assertEquals("Fred", v.getAuthor());
                assertEquals("1.2", v.getVersion());

                versionEffectiveDate=timeTwo;
                v=(Version)core.newType("SetVersion");
                assertEquals("Harry", v.getAuthor());
                assertEquals("1.3", v.getVersion());
            }
        }
        catch(Throwable e) {
            fail("Unexpected exception:"+e);
        }
    }

    /**
     * The BeanShellFactory provides support for types to be created by scripts
     * embedded in the systems configuration. This test checks that the factory
     * responsible for processing these scripts is working.<p>
     * The configuration used in this test defines two BeanShell scripted types, 
     * one (BeanShellVersion) should create a version object with author and version attributes set,
     * the other (BadBeanShellVersion) contains a syntax error which should cause a FactoryConfigurationError.
     * <ol>
     * <li>Create an instance of BeanShellVersion.</li>
     * <li>Fail if the author attribute is not set correctly.</li>
     * <li>Fail if the version attribute is not set correctly.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * <li>Create an instance of BadBeanShellVersion.</li>
     * <li>Fail if no exception is thrown.</li>
     * <li>Fail if any exception other than FactoryConfigurationError is thrown.</li>
     * </ol>
     */
    public void testBeanShellFactory() throws Exception {
        Version v=null;

        core.setConfiguration(this.createConfigurationOne());
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        v=(Version)core.newType("BeanShellVersion");
        assertEquals("Jim", v.getAuthor());
        assertEquals("1.3", v.getVersion());

        try {
            v=(Version)core.newType("BadBeanShellVersion");
            fail("Didn't get expected FactoryConfigurationError");
        }
        catch(FactoryConfigurationError e ) {
            // ignore this, its what we want to see
        }
    }

    /**
     * The BeanShellFactory supports the notion of a type creation script
     * that referrs to the 'core' - i.e. the script that creates the type
     * is given an instance of the core which it may use to access any
     * of the normal core features - like logging, of the type factory.
     * This test ensures that this feature us working.
     * <ol>
     * <li>Create an instance of BeanShellVersionCoreRef (whose factory script simply uses the core to create an instance of the 'BeanShellVersion' type)..</li>
     * <li>Fail if the author attribute is not set correctly.</li>
     * <li>Fail if the version attribute is not set correctly.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testBeanShellFactoryCoreRef() throws Exception {
        Version v=null;

        core.setConfiguration(this.createConfigurationOne());
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        v=(Version)core.newType("BeanShellVersionCoreRef");
        assertEquals("Jim", v.getAuthor());
        assertEquals("1.3", v.getVersion());
    }
    
    /**
     * The CastorXMLFactory provides support for types to be created via
     * embedded XML in the system configuration. This test checks that the factory
     * responsible for processing these XML types scripts is working.<p>
     * <ol>
     * <li>Create an instance of CastorXMLVersion.</li>
     * <li>Fail if the author attribute is not set correctly.</li>
     * <li>Fail if the version attribute is not set correctly.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testCastorXMLFactory() throws Exception {
        Version v=null;

        core.setConfiguration(this.createConfigurationOne());
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        v=(Version)core.newType("CastorXMLVersion");
        assertEquals("T.S.Elliot", v.getAuthor());
        assertEquals("1.0", v.getVersion());
        assertEquals("Peach and mint", v.getSource());
        assertEquals("state", v.getState());
        assertEquals("14/10/2002", v.getDate());
        assertEquals("The loganberry's are sweet", v.getComment());
        assertEquals("Copyright us.", v.getCopyright());
    }

    /**
     * The XSLTFactory provides support for creating types from an XSLT. The type
     * definition contains an embedded XSLT which the factory runs to generate an
     * XML type definition (which can be used by the CastorXMLFactory). The XSLTFactory
     * optionally allows the type to define an "extends" parameter which names another
     * type which should be used as input to the XSLT. The extends type is instantiated, 
     * translated to XML and passed into the XSLT. This test ensures that this mechanism
     * is working.
     * <ol>
     * <li>Create an instance of XSLTVersion.</li>
     * <li>Fail if the author attribute is not set correctly.</li>
     * <li>Fail if the version attribute is not set correctly.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testXSLTFactoryWithExtends() throws Exception {
        Version v=null;

        Configuration config=this.createConfigurationOne();
        config.setTimeout(5000);
        core.setConfiguration(config);
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        for(int i=0 ; i<5 ; i++) {
          long startAt=System.currentTimeMillis();
          v=(Version)core.newType("XSLTVersion");
          long endAt=System.currentTimeMillis();
          
          assertEquals("T.S.Elliot", v.getAuthor());
          assertEquals("1.0", v.getVersion());
          assertEquals("orange and pineapple", v.getSource());
          assertEquals("state", v.getState());
          assertEquals("14/10/2002", v.getDate());
          assertEquals("The loganberry's are sweet", v.getComment());
          assertEquals("Copyright us.", v.getCopyright());
  
          System.out.println("testXSLTFactoryWithExtends ("+i+") took:"+(endAt-startAt)+"ms");
        }
    }

    /**
     * The CastorXMLFactory provides support for types to be created via
     * embedded XML in the system configuration. This test checks that the factory
     * responsible for processing these XML types scripts is working.<p>
     * <ol>
     * <li>Create an instance of CastorXMLVersion.</li>
     * <li>Fail if the author attribute is not set correctly.</li>
     * <li>Fail if the version attribute is not set correctly.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testXSLTFactoryWithoutExtends() throws Exception {
      Version v=null;

      Configuration config=this.createConfigurationOne();
      config.setTimeout(5000);
      core.setConfiguration(config);
      ConfigurationHandler.reset();
      versionEffectiveDate=new VersionEffectiveDate();

      for(int i=0 ; i<5 ; i++) {
        long startAt=System.currentTimeMillis();
        v=(Version)core.newType("XSLTVersionNoExtends");
        long endAt=System.currentTimeMillis();
        
        assertEquals("T.S.Elliot", v.getAuthor());
        assertEquals("1.0", v.getVersion());
        assertEquals("Peach and mint", v.getSource());
        assertEquals("state", v.getState());
        assertEquals("14/10/2002", v.getDate());
        assertEquals("The loganberry's are sweet", v.getComment());
        assertEquals("Copyright us.", v.getCopyright());
    
        System.out.println("testXSLTFactoryWithoutExtends ("+i+") took:"+(endAt-startAt)+"ms");
      }
    }


    /**
     * The BeanShellFactory provides support for creating types from a BeanShell script.
     * The type definition contains an embedded script which the factory runs to generate 
     * the type definition. It optionally allows the type to define an "extends" parameter
     * which names another type which should be used as input to the script. The extends 
     * type is instantiated,and passed into the script as a variable named "type". This 
     * test ensures that this mechanism is working.
     * <ol>
     * <li>Create an instance of XSLTVersion.</li>
     * <li>Fail if the author attribute is not set correctly.</li>
     * <li>Fail if the version attribute is not set correctly.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    public void testBeanShellFactoryExtends() throws Exception {
      Version v=null;

      Configuration config=this.createConfigurationOne();
      config.setTimeout(5000);
      core.setConfiguration(config);
      ConfigurationHandler.reset();
      versionEffectiveDate=new VersionEffectiveDate();

      for(int i=0 ; i<5 ; i++) {
        long startAt=System.currentTimeMillis();
        v=(Version)core.newType("BeanShellExtendsVersion");
        long endAt=System.currentTimeMillis();
        
        assertEquals("T.S.Elliot", v.getAuthor());
        assertEquals("1.0", v.getVersion());
        assertEquals("Peach & lemon", v.getSource());
        assertEquals("state", v.getState());
        assertEquals("14/10/2002", v.getDate());
        assertEquals("The loganberry's are sweet", v.getComment());
        assertEquals("Copyright us.", v.getCopyright());
  
        System.out.println("testBeanShellFactoryExtends ("+i+") took :"+(endAt-startAt)+"ms");
      }
    }

    /**
     * Test to check that modifications to an object cloned in the factory do no
     * propogate to other instances of the same type.
     * @throws Exception
     */
    public void testFactoryClone() throws Exception {
        Version v=null;

        Configuration config=this.createConfigurationOne();
        config.setTimeout(50000);
        core.setConfiguration(config);
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        v=(Version)core.newType("CastorXMLVersion");
        assertEquals("valueone", v.getAttribute(0).getValue());
        assertEquals("valuetwo", v.getAttribute(1).getValue());
        
        v.getAttribute(0).setValue("newvalueone");

        Version v2=(Version)core.newType("CastorXMLVersion");
        assertEquals("valueone", v2.getAttribute(0).getValue());
        assertEquals("valuetwo", v2.getAttribute(1).getValue());
    }

    public void testFactorySingleInstance() throws Exception {

        Configuration config=this.createConfigurationOne();
        config.setTimeout(50000);
        core.setConfiguration(config);
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        Version v1=(Version)core.newType("CastorXMLVersionSingleInstance");
        
        Version v2=(Version)core.newType("CastorXMLVersionSingleInstance");
        
        assertTrue(v1==v2);

    }

    /**
     * The Factory's clone method should null transient properties - to avoid the
     * situation when all instances of the same type end up sharing one object.
     * This test checks this behaviour.
     * @throws Exception
     */
    public void testFactoryCloneOnTransientProperties() throws Exception {
        Configuration config=this.createConfigurationOne();
        config.setTimeout(50000);
        core.setConfiguration(config);
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        Version v=(Version)core.newType("CastorXMLVersion");
        
        System.out.println(v.fetchJXPathContext().getContextBean());

        v=(Version)core.newType("CastorXMLVersion");

        System.out.println(v.fetchJXPathContext().getContextBean());
    }

    /**
     * The Castor XML factory supports two type definition mechanisms. The normal "Script" 
     * mechanism, where the XML for the type definition is held with the type itself within
     * the 'Script' parameter; and the "Url" mechanism, where the XML is read from a URL 
     * which is pointed to by the type's 'Url' parameter.
     * This test exercises the Url mechanism.
     * @throws Exception
     */
    public void testCastorXMLFactoryUrlLoader() throws Exception {
        Version v=null;

        core.setConfiguration(this.createConfigurationOne());
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        v=(Version)core.newType("CastorXMLVersionByUrl");
        
        assertEquals("T.S.Elliot", v.getAuthor());
        assertEquals("1.0", v.getVersion());
        assertEquals("Peach and mint", v.getSource());
        assertEquals("state", v.getState());
        assertEquals("14/10/2002", v.getDate());
        assertEquals("The loganberry's are sweet", v.getComment());
        assertEquals("Copyright us.", v.getCopyright());
    }

    /**
     * The BeanShell factory supports two type definition mechanisms. The normal "Script" 
     * mechanism, where the script for the type definition is held with the type itself within
     * the 'Script' parameter; and the "Url" mechanism, where the script is read from a URL 
     * which is pointed to by the type's 'Url' parameter.
     * This test exercises the Url mechanism.
     * @throws Exception
     */
    public void testBeanShellFactoryUrlLoader() throws Exception {
        Version v=null;

        core.setConfiguration(this.createConfigurationOne());
        ConfigurationHandler.reset();
        versionEffectiveDate=new VersionEffectiveDate();

        v=(Version)core.newType("BeanShellVersionByUrl");
        assertEquals("Jim", v.getAuthor());
        assertEquals("1.3", v.getVersion());
    }
}
