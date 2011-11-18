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

import org.hibernate.classic.Session;

import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Type;
import com.ail.core.persistence.LoadArg;

/**
 * Implementation of the update service for Hibernate
 */
public class HibernateLoadService extends Service<LoadArg> {
    /** The 'business logic' of the entry point. */
    @Override
    public void invoke() throws PreconditionException, PostconditionException {

        // check arguments
	    if (args.getTypeArg()==null) {
	        throw new PreconditionException("args.getTypeArg()==null");
	    }

        Session session=null;
        
        session = HibernateFunctions.getSessionFactory().getCurrentSession(); 

        Type ret=(Type)session.load(args.getTypeArg(), args.getSystemIdArg());

        args.setObjectRet(ret);
        
		// loaded object must not be null
		if (args.getObjectRet()==null) {
			throw new PostconditionException("args.getObjectRet()==null");			
		}

    }
}

