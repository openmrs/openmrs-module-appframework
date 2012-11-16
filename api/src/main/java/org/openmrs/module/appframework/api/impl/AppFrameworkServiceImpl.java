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

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Privilege;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.springframework.util.StringUtils;

/**
 * It is a default implementation of {@link AppFrameworkService}.
 */
public class AppFrameworkServiceImpl extends BaseOpenmrsService implements AppFrameworkService {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	private List<AppDescriptor> allApps = new ArrayList<AppDescriptor>();

	/**
	 * @see org.openmrs.module.appframework.api.AppFrameworkService#ensurePrivilegeExists(org.openmrs.module.appframework.AppDescriptor)
	 */
	@Override
	public Privilege ensurePrivilegeExists(AppDescriptor app) {
	    String privName = app.getRequiredPrivilegeName();
	    Privilege priv = Context.getUserService().getPrivilege(privName);
	    if (priv == null) {
	    	priv = new Privilege();
	    	priv.setPrivilege(app.getRequiredPrivilegeName());
	    	priv.setDescription("Run the " + app.getLabel() + " app");
	    	Context.getUserService().savePrivilege(priv);
	    }
	    return priv;
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
        Collections.sort(allApps, new AppDescriptorComparator());
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
    	Map<String, AppDescriptor> appMap = new LinkedHashMap<String, AppDescriptor>();
    	for (AppDescriptor app : getAllApps()) {
    		if (app.getRequiredPrivilegeName() == null || user.hasPrivilege(app.getRequiredPrivilegeName())) {
    			appMap.put(app.getId(), app);
    		}
    	}
    	
    	// re-sort this list according to user property
    	String sortOrder = user.getUserProperty(AppFrameworkConstants.APP_SORT_ORDER_USER_PROPERTY);
    	if (StringUtils.hasLength(sortOrder)) {
    		List<AppDescriptor> ret = new ArrayList<AppDescriptor>();

    		// loop over the sort order and add to new list in that order
    		List<String> sortedIds = new ArrayList<String>();
    		for (String id : sortOrder.split(",")) {
    			AppDescriptor app = appMap.get(id);
    			if (app != null) {
    				ret.add(app);
    				appMap.remove(id);
    			}
    		}
    		
    		// add all apps that weren't in the sort order for some reason
    		ret.addAll(appMap.values());
    		
    		return ret;
    	}
    	else
    		return new ArrayList<AppDescriptor>(appMap.values());
    	
    }

    private static class AppDescriptorComparator implements Comparator<AppDescriptor> {
        @Override
        public int compare(AppDescriptor appDescriptor, AppDescriptor appDescriptor1) {
            Integer firstAppOrder = appDescriptor.getOrder();
            Integer secondAppOrder = appDescriptor1.getOrder();
            if(firstAppOrder == null) {
                if(secondAppOrder == null) {
                    return 0;
                } else {
                    return 1;
                }
            } else if(secondAppOrder == null) {
                return -1;
            }
            return firstAppOrder.compareTo(secondAppOrder);
        }
    }
}