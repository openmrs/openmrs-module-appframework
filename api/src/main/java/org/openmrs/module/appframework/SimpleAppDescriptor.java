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
 * Basic implementation of AppDescriptor, suitable for subclassing
 */
public class SimpleAppDescriptor implements AppDescriptor {
	
	protected String id;
	protected String label;
	protected String iconUrl;
	protected String tinyIconUrl;
	protected String homepageUrl;
	
	/**
	 * Default constructor 
	 */
	public SimpleAppDescriptor() {
	}
	
    /**
     * @param id
     * @param label
     */
    public SimpleAppDescriptor(String id, String label) {
	    this.id = id;
	    this.label = label;
    }

	/**
     * @see org.openmrs.module.appframework.AppDescriptor#getId()
     */
	@Override
    public String getId() {
    	return id;
    }
	
    /**
     * @param id the id to set
     */
    public void setId(String id) {
    	this.id = id;
    }

    /**
     * @see org.openmrs.module.appframework.AppDescriptor#getLabel()
     */
    @Override
    public String getLabel() {
    	return label;
    }
	
    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
    	this.label = label;
    }
	
    /**
     * @see org.openmrs.module.appframework.AppDescriptor#getIconUrl()
     */
    @Override
    public String getIconUrl() {
    	return iconUrl;
    }
	
    /**
     * @param iconUrl the iconUrl to set
     */
    public void setIconUrl(String iconUrl) {
    	this.iconUrl = iconUrl;
    }

    /**
     * @see org.openmrs.module.appframework.AppDescriptor#getTinyIconUrl()
     */
    @Override
    public String getTinyIconUrl() {
    	return tinyIconUrl;
    }
	
    /**
     * @param tinyIconUrl the tinyIconUrl to set
     */
    public void setTinyIconUrl(String tinyIconUrl) {
    	this.tinyIconUrl = tinyIconUrl;
    }

    /**
     * @see org.openmrs.module.appframework.AppDescriptor#getHomepageUrl()
     */
    @Override
    public String getHomepageUrl() {
    	return homepageUrl;
    }
	
    /**
     * @param homepageUrl the homepageUrl to set
     */
    public void setHomepageUrl(String homepageUrl) {
    	this.homepageUrl = homepageUrl;
    }

    /**
     * @see org.openmrs.module.appframework.AppDescriptor#startApp(org.openmrs.module.appframework.AppStatus)
     */
    @Override
    public void startApp(AppStatus status) {
        // do nothing
    }
    
    /**
     * @see org.openmrs.module.appframework.AppDescriptor#endApp(org.openmrs.module.appframework.AppStatus)
     */
    @Override
    public void endApp(AppStatus status) {
        // do nothing
    }
    
    /**
     * @see org.openmrs.module.appframework.AppDescriptor#getRequiredPrivilegeName()
     */
    @Override
    public String getRequiredPrivilegeName() {
        return "App: " + getId();
    }
}
