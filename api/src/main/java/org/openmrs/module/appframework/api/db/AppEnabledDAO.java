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
package org.openmrs.module.appframework.api.db;

import java.util.List;

import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.module.appframework.AppEnabled;
import org.openmrs.module.appframework.api.AppFrameworkService;

/**
 *  Database methods for {@link AppFrameworkService} and {@link AppEnabled}.
 */
public interface AppEnabledDAO extends SingleClassDAO<AppEnabled> {

	public AppEnabled getByUserAndApp(User user, String appName);
	
	public AppEnabled getByRoleAndApp(Role role, String appName);
	
	/**
	 * Gets all apps that are enabled for this user, either directly or via one of their roles
	 * 
	 * @param forUser
	 * @return names of all apps enabled for forUser
	 */
	public List<String> getEnabledAppsForUser(User forUser);
	
	/**
	 * Gets all apps that are enabled for the given role
	 * 
	 * @param role
	 * @return names of all apps enabled for role
	 */
	public List<String> getEnabledAppsForRole(Role role);
	
}