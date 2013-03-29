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


import java.io.IOException;
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
import org.openmrs.module.appframework.loader.AppConfigurationLoader;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class AppFrameworkActivator extends BaseModuleActivator implements ModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());

    @Autowired
    private AppConfigurationLoader appConfigurationLoader;

	/**
	 * @see ModuleActivator#contextRefreshed()
	 * @should set all available apps on {@link AppFrameworkService}
	 * @should create privileges for all available apps
	 */
	public void contextRefreshed() {
		System.out.println("IN MODULE ACTIVATOR...");
        List<AppConfigurationLoader> configurationLoaders = Context.getRegisteredComponents(AppConfigurationLoader.class);
        AppConfigurationLoader loader = configurationLoaders.get(0);

        try {
//            appConfigurationLoader.loadConfiguration();
            loader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("Error reading app framework configuration", e);
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
