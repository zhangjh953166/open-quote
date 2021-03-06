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

package com.ail.core;

/**
 * Core specific wrapper for the java.util.Locale class. This class simply wraps a Java Locale object and
 * adds a number of helper methods to make it more suitable for use within the core. Not all of Java's 
 * Locale methods are exposed here, but access is provided to the wrapped instance via {@link #getInstance()}.
 * @see java.util.Locale
 */
public class Locale extends Type {
    private static ThreadLocal<java.util.Locale> threadLocale = new ThreadLocal<java.util.Locale>() {
        java.util.Locale threadLocale;
        
        public java.util.Locale get() {
            return threadLocale!=null ? threadLocale : java.util.Locale.getDefault();
        }
        
        public void set(java.util.Locale threadLocale) {
            this.threadLocale=threadLocale;
        }
    };
    
    private String language=null;
    private String country=null;
    private String variant=null;
    private transient java.util.Locale locale=null;
    
    /**
     * Default constructor
     */
    public Locale() {
        super();
    }

    /**
     * @see java.util.Locale#Locale(String)
     * @param language
     */
    public Locale(String language) {
        super();
        this.language = language;
    }

    /**
     * @see java.util.Locale#Locale(String, String)
     * @param language
     * @param country
     */
    public Locale(String language, String country) {
        super();
        this.language = language;
        this.country = country;
    }

    /**
     * @see java.util.Locale#Locale(String, String, String)
     * @param language
     * @param country
     * @param variant
     */
    public Locale(String language, String country, String variant) {
        super();
        this.language = language;
        this.country = country;
        this.variant = variant;
    }
    
    public Locale(java.util.Locale locale) {
        super();
        this.language=locale.getLanguage();
        this.country=locale.getCountry();
        this.variant=locale.getVariant();
    }

    /**
     * @see java.util.Locale#getLanguage()
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language associated with this Locale. This has the effect of invalidating any
     * existing references to {@link Locale#getInstance()}.
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
        locale=null;
    }

    /**
     * @see java.util.Locale#getCountry()
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country associated with this Locale. This has the effect of invalidating any
     * existing references to {@link Locale#getInstance()}.
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
        locale=null;
    }

    /**
     * @see java.util.Locale#getVariant()
     * @return the variant
     */
    public String getVariant() {
        return variant;
    }

    /**
     * Set the variant associated with this Locale. This has the effect of invalidating any
     * existing references to {@link Locale#getInstance()}.
     * @param variant the variant to set
     */
    public void setVariant(String variant) {
        this.variant = variant;
        locale=null;
    }

    /**
     * Get the wrapped instance of java.util.Locale representing this Locale.
     * @return wrapped instance
     */
    public java.util.Locale getInstance() {
        if (locale==null) {
            if (language!=null && country!=null && variant!=null) {
                locale=new java.util.Locale(language, country, variant);
            }
            else if (language!=null && country!=null && variant==null) {
                locale=new java.util.Locale(language, country);
            }
            else if (language!=null && country==null && variant==null) {
                locale=new java.util.Locale(language);
            }
            else  {
                throw new IllegalStateException("Cannot construct a locale from: language='"+language+"' country='"+country+"', variant='"+variant+"'");
            }
        }
        return locale;
    }

    /**
     * Get an instance of Locale representing the default locale for this JVM.
     * @see java.util.Locale#getDefault()
     * @return JVM's default locale
     */
    public static Locale getDefault() {
        return new Locale(java.util.Locale.getDefault());
    }

    /**
     * Get an instance of Locale representing the default locale for this JVM.
     * @see java.util.Locale#getDefault()
     * @return JVM's default locale
     */
    public static void setDefault(Locale locale) {
        java.util.Locale.setDefault(locale.getInstance());
    }

    /**
     * Set the locale to be used while processing this thread.
     * @param threadLocaleArg Locale to be used
     */
    public static void setThreadLocale(java.util.Locale threadLocaleArg) {
        threadLocale.set(threadLocaleArg);
    }

    /**
     * Get the locale being used while processing this thread.
     * @return Locale currently being used
     */
    public static java.util.Locale getThreadLocale() {
        return threadLocale.get();
    }
}
