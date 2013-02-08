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


import org.openmrs.messagesource.MessageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Basic implementation of AppDescriptor, suitable for subclassing
 */
public class SimpleAppDescriptor implements AppDescriptor {

    @Autowired
    @Qualifier("messageSourceService")
    MessageSourceService messageSourceService;

    protected String id;
	protected String label;
    protected String labelCode;
	protected String iconUrl;
	protected String tinyIconUrl;
	protected String homepageUrl;
    protected Integer order;
    protected String requiredPrivilegeName;

    /**
	 * Default constructor
	 */
	public SimpleAppDescriptor() {
	}

    public SimpleAppDescriptor(String id, String label, Integer order) {
	    this.id = id;
	    this.label = label;
        this.order = order;
    }

    /**
     * @param id
     * @param label
     */
    public SimpleAppDescriptor(String id, String label) {
        this(id, label, null);
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
        if (label != null) {
    	    return label;
        } else if (labelCode != null) {
            return messageSourceService.getMessage(labelCode);
        } else {
            return null;
        }
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

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
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

    public void setRequiredPrivilegeName(String requiredPrivilegeName) {
        this.requiredPrivilegeName = requiredPrivilegeName;
    }

    /**
     * @see org.openmrs.module.appframework.AppDescriptor#getRequiredPrivilegeName()
     */
    @Override
    public String getRequiredPrivilegeName() {
        if(requiredPrivilegeName == null) {
            requiredPrivilegeName = "App: " + getId();
        }
        return requiredPrivilegeName;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }
}
