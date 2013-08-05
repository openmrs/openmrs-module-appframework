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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.factory.AppFrameworkFactory;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllAppTemplates;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.openmrs.module.appframework.service.AppFrameworkService;

import java.util.List;

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
        // I dislike this way of pulling in services and components. Ideally they should be pulled in
        // using proper IOC. It is not possible for this class since the activator of each module is created
        // in openmrs-core via newInstance() which doesn't instantiate it in a spring way. This should
        // later be changed.

        List<AppFrameworkFactory> appFrameworkFactories = Context.getRegisteredComponents(AppFrameworkFactory.class);
        AllAppTemplates allAppTemplates = Context.getRegisteredComponents(AllAppTemplates.class).get(0);
        AllAppDescriptors allAppDescriptors = Context.getRegisteredComponents(AllAppDescriptors.class).get(0);
        AllExtensions allExtensions = Context.getRegisteredComponents(AllExtensions.class).get(0);

        registerAppsAndExtensions(appFrameworkFactories, allAppTemplates, allAppDescriptors, allExtensions);
    }

    public void registerAppsAndExtensions(List<AppFrameworkFactory> appFrameworkFactories, AllAppTemplates allAppTemplates, AllAppDescriptors allAppDescriptors, AllExtensions allExtensions) {
        for (AppFrameworkFactory appFrameworkFactory : appFrameworkFactories) {
            try {
                allAppTemplates.clear();
                List<AppTemplate> appTemplates = appFrameworkFactory.getAppTemplates();
                allAppTemplates.add(appTemplates);

                allAppDescriptors.clear();
                List<AppDescriptor> appDescriptors = appFrameworkFactory.getAppDescriptors();
                allAppDescriptors.add(appDescriptors);

                allExtensions.clear();
                List<Extension> extensions = appFrameworkFactory.getExtensions();
                allExtensions.add(extensions);

                allAppDescriptors.setAppTemplatesOnInstances(allAppTemplates);
                allAppDescriptors.setExtensionApps();
            }
            catch (Exception e) {
                log.error("Error loading app framework. Some apps might not work." + appFrameworkFactory, e);
            }
        }
    }

}
