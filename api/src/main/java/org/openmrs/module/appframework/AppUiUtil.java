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
package org.openmrs.module.appframework;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.openmrs.ui.framework.session.Session;


/**
 * Utility methods for using Apps in the UI Framework
 */
public class AppUiUtil {
	
	public final static String SESSION_ATTR_CURRENT_APP = "currentApp";

	/**
     * If necessary (i.e. it isn't already running) starts up a new copy of the given app.
     * (If another app was running, it is ended.)
     * 
     * @param appId
     * @param session
     */
    public static AppStatus startApp(String appId, Session session) {
    	if (appId == null)
    		throw new IllegalArgumentException("appId is required");

    	AppStatus currentApp = getCurrentApp(session);
    	if (currentApp != null) {
    		if (appId.equals(currentApp.getApp().getId())) {
    			return currentApp;
    		} else {
    			endCurrentApp(session);
    		}
    	}
    	
    	AppDescriptor app = Context.getService(AppFrameworkService.class).getAppById(appId);
    	if (app == null)
    		throw new IllegalArgumentException("Cannot find app with id " + appId);
    	AppStatus status = new AppStatus(app);
    	app.startApp(status);
    	session.setAttribute(SESSION_ATTR_CURRENT_APP, status);
    	return status;
    }

	/**
     * Clears the currently-running app form the session, and calls its appEnded callback.
     * 
     * @param session
     */
    public static void endCurrentApp(Session session) {
	    AppStatus currentApp = getCurrentApp(session);
	    if (currentApp != null) {
	    	session.setAttribute(SESSION_ATTR_CURRENT_APP, null);
	    	currentApp.getApp().endApp(currentApp);
	    }
    }

	/**
     * Gets the currently-running app from the session
     * 
     * @param session
     * @return
     */
    public static AppStatus getCurrentApp(Session session) {
    	try {
    		return session.getAttribute(SESSION_ATTR_CURRENT_APP, AppStatus.class);
    	} catch (ClassCastException ex) {
    		// this means the App Framework module has been reloaded
    		session.setAttribute(SESSION_ATTR_CURRENT_APP, null);
    		return null;
    	}
    }

	/**
     * If there is no currently-running app, then start the indicated one. Generally you should call
     * this method from the controller of any page that usually belongs to one app, but can be accessed
     * from other apps. 
     * 
     * @param appId
     * @param session
     */
    public static void startAppIfNone(String appId, Session session) {
	    if (getCurrentApp(session) == null)
	    	startApp(appId, session);
    }
	
}
