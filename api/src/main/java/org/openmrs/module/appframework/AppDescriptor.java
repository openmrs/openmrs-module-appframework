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


/**
 * Describes an "app", vaguely defined as something displayed in the UI as an icon and label, that can be
 * enabled per-user or per-group 
 */
public interface AppDescriptor {

	/**
     * @return a unique id for this app
     */
    String getId();

    /**
     * @return the user-facing name for this app
     */
    String getLabel();
    
    /**
     * @return the URL of an image to used for this app 
     */
    String getIconUrl();
    
    /**
     * @return the URL of a tiny image, e.g. to represent this app in the corner of the header 
     */
    String getTinyIconUrl();
    
    /**
     * Supports relative URLs against OpenMRS's context-path
     * @return the URL of this app's homepage
     */
    String getHomepageUrl();
    
    /**
     * Called when a new AppStatus is instantiated for this app.
     * 
     * @param status
     */
    void startApp(AppStatus status);
    
    /**
     * Called when the user chooses to end this app. (The app lifecycle is not well defined at this point
     * so you should avoid relying on this method being called to do resource cleanup.)
     * 
     * @param status
     */
    void endApp(AppStatus status);

    /**
     * The App Framework module will automatically create this privilege if it doesn't already exist
     * 
     * @return the name of the privilege that is required for a user to see this app
     */
    String getRequiredPrivilegeName();
}