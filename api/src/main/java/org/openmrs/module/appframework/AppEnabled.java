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

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Role;
import org.openmrs.User;


/**
 * Represents the fact that an app is enabled for a particular user or role.
 * You need to set {@link #appName} and exactly one of {@link #user} and {@link #role}
 */
public class AppEnabled extends BaseOpenmrsObject {
	
	private Integer appEnabledId;
	
	// the app to enable
	private String appName;

	// user XOR role should be set
	private User user;
	private Role role;

    public AppEnabled() {
    }
	
	/**
     * @param user
     * @param appName
     */
    public AppEnabled(User user, String appName) {
	    this.user = user;
	    this.appName = appName;
    }
    
	/**
     * @param role
     * @param appName
     */
    public AppEnabled(Role role, String appName) {
	    super();
	    this.role = role;
	    this.appName = appName;
    }

	/**
     * @see org.openmrs.OpenmrsObject#getId()
     */
    @Override
    public Integer getId() {
	    return getAppEnabledId();
    }

	/**
     * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
     */
    @Override
    public void setId(Integer id) {
	    setAppEnabledId(id);
    }
	
    /**
     * @return the appEnabledId
     */
    public Integer getAppEnabledId() {
    	return appEnabledId;
    }
	
    /**
     * @param appEnabledId the appEnabledId to set
     */
    public void setAppEnabledId(Integer appEnabledId) {
    	this.appEnabledId = appEnabledId;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
    	return appName;
    }
	
    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
    	this.appName = appName;
    }
	
    /**
     * @return the user
     */
    public User getUser() {
    	return user;
    }
	
    /**
     * @param user the user to set
     */
    public void setUser(User user) {
    	this.user = user;
    }
	
    /**
     * @return the role
     */
    public Role getRole() {
    	return role;
    }
	
    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
    	this.role = role;
    }

}
