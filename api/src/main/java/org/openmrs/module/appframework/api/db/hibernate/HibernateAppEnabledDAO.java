/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.appframework.api.db.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.module.appframework.AppEnabled;
import org.openmrs.module.appframework.api.db.AppEnabledDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hibernate-based implementation of  {@link AppEnabledDAO}.
 */
public class HibernateAppEnabledDAO extends HibernateSingleClassDAO<AppEnabled> implements AppEnabledDAO {

	protected final Log log = LogFactory.getLog(this.getClass());

	public HibernateAppEnabledDAO() {
		super(AppEnabled.class);
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.AppEnabledDAO#getEnabledAppsForUser(org.openmrs.User)
	 * @should get apps enabled directly for user
	 * @should get apps enabled for role
	 * @should get apps enabled for parent role
	 */
	@SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
	public List<String> getEnabledAppsForUser(User forUser) {
		Set<Role> roles = forUser.getAllRoles();
		if (roles.size() > 0) {
			Query query = session().createQuery("select distinct appName from AppEnabled where user = :user or role in (:roles)");
			query.setParameter("user", forUser);
			query.setParameterList("roles", roles);
		    return (List<String>) query.list();
		} else {
			Query query = session().createQuery("select distinct appName from AppEnabled where user = :user");
			query.setParameter("user", forUser);
		    return (List<String>) query.list();
		}
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.AppEnabledDAO#getEnabledAppsForRole(org.openmrs.Role)
	 */
	@SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
	public List<String> getEnabledAppsForRole(Role role) {
		Query query = session().createQuery("select distinct appName from AppEnabled where role = :role");
		query.setEntity("role", role);
	    return (List<String>) query.list();
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.AppEnabledDAO#getByRoleAndApp(org.openmrs.Role, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public AppEnabled getByRoleAndApp(Role role, String appName) {
	    Criteria crit = session().createCriteria(AppEnabled.class);
	    crit.add(Restrictions.eq("role", role));
	    crit.add(Restrictions.eq("appName", appName));
	    return (AppEnabled) crit.uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.appframework.api.db.AppEnabledDAO#getByUserAndApp(org.openmrs.User, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public AppEnabled getByUserAndApp(User user, String appName) {
		Criteria crit = session().createCriteria(AppEnabled.class);
	    crit.add(Restrictions.eq("user", user));
	    crit.add(Restrictions.eq("appName", appName));
	    return (AppEnabled) crit.uniqueResult();
	}
	
}