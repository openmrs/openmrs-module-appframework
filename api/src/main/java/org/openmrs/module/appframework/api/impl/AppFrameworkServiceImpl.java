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
package org.openmrs.module.appframework.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.AppEnabled;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.openmrs.module.appframework.api.db.AppEnabledDAO;

/**
 * It is a default implementation of {@link AppFrameworkService}.
 */
public class AppFrameworkServiceImpl extends BaseOpenmrsService implements AppFrameworkService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private AppEnabledDAO appEnabledDao;
	
	private List<AppDescriptor> allApps = new ArrayList<AppDescriptor>();

    /**
     * @return the appEnabledDao
     */
    public AppEnabledDAO getAppEnabledDao() {
    	return appEnabledDao;
    }

    /**
     * @param appEnabledDao the appEnabledDao to set
     */
    public void setAppEnabledDao(AppEnabledDAO appEnabledDao) {
    	this.appEnabledDao = appEnabledDao;
    }
    
    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#enableAppForUser(java.lang.String, org.openmrs.User)
     */
    @Override
    public void enableAppForUser(String appName, User user) {
    	AppEnabled e = appEnabledDao.getByUserAndApp(user, appName);
    	if (e == null)
    		appEnabledDao.create(new AppEnabled(user, appName));
    }
    
    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#disableAppForUser(java.lang.String, org.openmrs.User)
     */
    @Override
    public void disableAppForUser(String appName, User user) {
    	AppEnabled e = appEnabledDao.getByUserAndApp(user, appName);
    	if (e != null)
    		appEnabledDao.delete(e);
    }
    
    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#enableAppForRole(java.lang.String, org.openmrs.Role)
     */
    @Override
    public void enableAppForRole(String appName, Role role) {
    	AppEnabled e = appEnabledDao.getByRoleAndApp(role, appName);
    	if (e == null)
    		appEnabledDao.create(new AppEnabled(role, appName));
    }
    
    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#disableAppForRole(java.lang.String, org.openmrs.Role)
     */
    @Override
    public void disableAppForRole(String appName, Role role) {
    	AppEnabled e = appEnabledDao.getByRoleAndApp(role, appName);
    	if (e != null)
    		appEnabledDao.delete(new AppEnabled(role, appName));
    }

	
    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#getAllApps()
     */
    @Override
    public List<AppDescriptor> getAllApps() {
    	return allApps;
    }

    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#setAllApps(java.util.List)
     */
    @Override
    public void setAllApps(List<AppDescriptor> allApps) {
    	this.allApps = allApps;
    }
    
    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#getAppById(java.lang.String)
     */
    @Override
    public AppDescriptor getAppById(String id) {
        for (AppDescriptor app : allApps)
        	if (app.getId().equals(id))
        		return app;
        return null;
    }

    /**
     * @see org.openmrs.module.appframework.api.AppFrameworkService#getAppsForUser(org.openmrs.User)
     */
    @Override
    public List<AppDescriptor> getAppsForUser(User user) {
        Set<String> enabledIds = new HashSet<String>(appEnabledDao.getEnabledAppsForUser(user));
        List<AppDescriptor> ret = new ArrayList<AppDescriptor>();
        for (AppDescriptor app : getAllApps()) {
        	if (enabledIds.contains(app.getIconUrl()))
        		ret.add(app);
        }
        return ret;
    }
    
}