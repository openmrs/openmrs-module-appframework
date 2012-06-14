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
package org.openmrs.module.appframework.api;

import java.util.List;

import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.appframework.AppDescriptor;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(AppFrameworkService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
public interface AppFrameworkService extends OpenmrsService {

	/**
	 * Creates a privilege for app, if it doesn't already exist
	 * 
	 * @param app
	 * @return the existing or newly-created privilege
	 */
	@Transactional
	Privilege ensurePrivilegeExists(AppDescriptor app);
		
	/**
     * Sets the complete list of apps available.
     * 
     * You should not need to call this--the framework calls this itself after modules are loaded. 
     * 
     * @param apps
     */
    void setAllApps(List<AppDescriptor> apps);
    
    /**
     * Please do not modify the contents of the returned list.
     * @return all available apps (irrespective of whether or not they're enabled for users and roles)
     */
    List<AppDescriptor> getAllApps();
	
    /**
     * @param id
     * @return the app with the given unique id
     */
    AppDescriptor getAppById(String id);

    /**
     * @param user
     * @return all apps that are enabled for the given user
     * @should get apps that a particular user has privileges for
     */
    List<AppDescriptor> getAppsForUser(User user);
    
}