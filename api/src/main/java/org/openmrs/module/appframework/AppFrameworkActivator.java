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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.appframework.api.AppFrameworkService;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class AppFrameworkActivator extends BaseModuleActivator implements ModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		
	/**
	 * @see ModuleActivator#contextRefreshed()
	 * @should set all available apps on {@link AppFrameworkService}
	 * @should create privileges for all available apps
	 */
	public void contextRefreshed() {
		AppFrameworkService service = Context.getService(AppFrameworkService.class);
		
		List<AppDescriptor> apps = new ArrayList<AppDescriptor>();
		apps.addAll(Context.getRegisteredComponents(AppDescriptor.class));

		for (AppFactory factory : Context.getRegisteredComponents(AppFactory.class)) {
			apps.addAll(factory.getAppDescriptors());
		}
		
		Set<String> ids = new HashSet<String>();
		for (AppDescriptor app : apps) {
			if (ids.contains(app.getId()))
				log.warn("Found multiple apps with id: " + app.getId());
			else
				ids.add(app.getId());
		}
		
		service.setAllApps(apps);
		for (AppDescriptor app : apps) {
			service.ensurePrivilegeExists(app);
		}
		
		log.info("App Framework Module refreshed: " + apps.size() + " apps available");
		if (log.isDebugEnabled()) {
			for (AppDescriptor app : apps)
				log.debug(app.getLabel() + " (" + app.getId() + ")");
		}
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		log.info("App Framework Module started");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("App Framework Module stopped");
	}
		
}
