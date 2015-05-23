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
package org.openmrs.module.appframework.service;

import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.appframework.context.AppContextModel;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.UserApp;

import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured
 * in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(AppFrameworkService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
public interface AppFrameworkService extends OpenmrsService {
	
	List<AppDescriptor> getAllApps();

	List<Extension> getAllExtensions(String extensionPointId);

    List<Extension> getExtensionsById(String extensionPointId, String id);
	
	List<AppDescriptor> getAllEnabledApps();
	
	/**
	 * Gets all enabled extensions for the specified extensionPointId
	 * 
	 * @param extensionPointId the extensionPointId to match against
	 * @return a list of Extensions
	 * @should get all extensions for the specified extensionPointId
	 */
	List<Extension> getAllEnabledExtensions(String extensionPointId);

	List<Extension> getAllEnabledExtensions();

	void enableApp(String appId);
	
	void disableApp(String appId);
	
	void enableExtension(String extensionId);
	
	void disableExtension(String extensionId);
	
	/**
	 * Gets all enabled extensions for the currently logged in user
	 * 
	 * @return a list of Extensions
	 * @should get all enabled extensions for the currently logged in user
     * @should return extensions with no required privilege if there is no authenticated user
	 */
	List<Extension> getExtensionsForCurrentUser();
	
	/**
	 * Gets all enabled extensions for the currently logged in user for the specified
	 * extensionPointId
	 * 
	 * @param extensionPointId the extension point id to match against
	 * @return a list of Extensions
	 * @should get all enabled extensions for the logged in user and extensionPointId
	 */
	List<Extension> getExtensionsForCurrentUser(String extensionPointId);

    /**
     * Gets enabled extensions points for the currently logged in user, for the specified extensionPointId. If any
     * extension has a "require" attribute, and contextModel is not null, we evaluate the extension's require string
     * against contextModel.
     * @param extensionPointId
     * @param contextModel
     * @return
     * @should get enabled extensions for the current user whose require property matches the contextModel
     */
    List<Extension> getExtensionsForCurrentUser(String extensionPointId, AppContextModel appContextModel);

    /**
	 * Gets all enabled apps for the currently logged in user
	 * 
	 * @return a list of Extensions
	 * @should get all enabled apps for the currently logged in user
     * @should return apps with no required privilege if there is no authenticated user
	 */
	List<AppDescriptor> getAppsForCurrentUser();

    /**
     * Gets all non-retired locations that are tagged as supporting logins.
     * @see org.openmrs.module.appframework.AppFrameworkConstants#LOCATION_TAG_SUPPORTS_LOGIN
     * @return all locations that you can choose as a sessionLocation when logging in
     */
    List<Location> getLoginLocations();

    /**
     * @return all app templates
     */
    List<AppTemplate> getAllAppTemplates();

    /**
     * Gets an app template by its id
     * @param id
     * @return
     */
    AppTemplate getAppTemplate(String id);

    /**
     * Gets an app by its id
     * @param id
     * @return
     * @should return a user app that matches the specified id
     */
    AppDescriptor getApp(String id);

    /**
     * Gets a UserApp that matches the specified appId
     *
     * @since 2.3
     * @param appId
     * @return UserApp
     * @should return a user app that matches the specified appId
     */
    UserApp getUserApp(String appId);

    /**
     * Gets all UserApps
     *
     * @since 2.3
     * @return List<UserApp>
     * @should return a list of UserApps
     */
    List<UserApp> getUserApps();

    /**
     * Saves a new UserApp to the database or updates it in the DB in case it is an existing one
     *
     * @since 2.3
     * @param userApp the UserApp to save
     * @return UserApp
     * @should save the user app to the database and update the list of loaded apps
     */
    UserApp saveUserApp(UserApp userApp);

    /**
     * Deletes the UserApp from the database
     *
     * @since 2.3
     * @param userApp the UserApp to purge
     * @should remove the user app from the database and update the list of loaded apps
     */
    void purgeUserApp(UserApp userApp);

}
