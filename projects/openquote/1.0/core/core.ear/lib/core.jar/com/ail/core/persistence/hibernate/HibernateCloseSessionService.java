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

package com.ail.core.persistence.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.classic.Session;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.core.persistence.CloseSessionArg;
import com.ail.core.persistence.UpdateException;

/**
 * Implemention of the open session service for Hibernate
 * 
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/08/20 15:03:54 $
 * @source $Source:
 *         /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/hibernate/HibernateUpdateService.java,v $
 * @stereotype service
 */
public class HibernateCloseSessionService extends Service {
    private CloseSessionArg args = null;

    private Core core;

    /** Default constructor */
    public HibernateCloseSessionService() {
        super();
        core = new Core(this);
    }

    /**
     * Getter to fetch the entry point's core. This method is demanded by the EntryPoint class.
     * 
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * 
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) getCore().newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2006/08/20 15:03:54 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/hibernate/HibernateCloseSessionService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.2 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * 
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (CloseSessionArg) args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * 
     * @return An instance of UpdateArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, UpdateException {
        Session session=null;

        try {
            session=HibernateFunctions.getSessionFactory().getCurrentSession();
            session.flush();
        }
        catch (StaleObjectStateException e) {
            throw new UpdateException(e.toString(), e);
        }
        catch (HibernateException e) {
            throw new UpdateException(e.toString(), e);
        }
        finally {
            session.close();
        }
    }
}
