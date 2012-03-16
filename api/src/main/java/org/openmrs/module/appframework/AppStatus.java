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

import java.util.HashMap;
import java.util.Map;


/**
 * Contains the status of an App that's being run by the user.
 */
public class AppStatus {
	
	private AppDescriptor app;
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public AppStatus(AppDescriptor app) {
		this.app = app;
	}
	
    /**
     * @return the app
     */
    public AppDescriptor getApp() {
    	return app;
    }
	
    /**
     * @param app the app to set
     */
    public void setApp(AppDescriptor app) {
    	this.app = app;
    }
	
    /**
     * @return the data
     */
    public Map<String, Object> getData() {
    	return data;
    }
	
    /**
     * @param data the data to set
     */
    public void setData(Map<String, Object> data) {
    	this.data = data;
    }
	
}
