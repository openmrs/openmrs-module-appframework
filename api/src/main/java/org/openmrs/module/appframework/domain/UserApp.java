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
package org.openmrs.module.appframework.domain;

import java.io.Serializable;

/**
 * User to encapsulate data about a user defined app that is stored in the database
 * 
 * @since 2.3
 */
public class UserApp implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String appId;
	
	private String json;
	
	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getJson() {
		return json;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this == obj
		        || (obj instanceof UserApp && getAppId() != null && ((UserApp) obj).getAppId().equals(this.getAppId()));
		
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getAppId() == null) {
			return super.hashCode();
		}
		return getAppId().hashCode();
	}
	
}
