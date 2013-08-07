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
package org.openmrs.module.appframework.service;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.ComponentState;
import org.openmrs.module.appframework.domain.ComponentType;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllAppTemplates;
import org.openmrs.module.appframework.repository.AllComponentsState;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * It is a default implementation of {@link AppFrameworkService}.
 */
public class AppFrameworkServiceImpl extends BaseOpenmrsService implements AppFrameworkService {

    private AllAppTemplates allAppTemplates;

	private AllAppDescriptors allAppDescriptors;
	
	private AllExtensions allExtensions;
	
	private AllComponentsState allComponentsState;

    private LocationService locationService;

    public AppFrameworkServiceImpl(AllAppTemplates allAppTemplates, AllAppDescriptors allAppDescriptors, AllExtensions allExtensions,
	    AllComponentsState allComponentsState, LocationService locationService) {
        this.allAppTemplates = allAppTemplates;
		this.allAppDescriptors = allAppDescriptors;
		this.allExtensions = allExtensions;
		this.allComponentsState = allComponentsState;
        this.locationService = locationService;
	}
	
	@Override
	public List<AppDescriptor> getAllApps() {
		return allAppDescriptors.getAppDescriptors();
	}
	
	@Override
	public List<Extension> getAllExtensions(String extensionPointId) {
        List<Extension> matchingExtensions = new ArrayList<Extension>();
		for (Extension extension : allExtensions.getExtensions()) {
			if (extensionPointId == null || extension.getExtensionPointId().equalsIgnoreCase(extensionPointId))
				matchingExtensions.add(extension);
		}
		return matchingExtensions;
	}
	
	@Override
	public List<AppDescriptor> getAllEnabledApps() {
		List<AppDescriptor> appDescriptors = getAllApps();
		
		ComponentState componentState;
		List<AppDescriptor> disabledAppDescriptors = new ArrayList<AppDescriptor>();
		for (AppDescriptor appDescriptor : appDescriptors) {
			componentState = allComponentsState.getComponentState(appDescriptor.getId(), ComponentType.APP);
			if (componentState != null && !componentState.getEnabled())
				disabledAppDescriptors.add(appDescriptor);
		}
		
		appDescriptors.removeAll(disabledAppDescriptors);
		return appDescriptors;
	}
	
	/**
	 * @see org.openmrs.module.appframework.service.AppFrameworkService#getAllEnabledExtensions(java.lang.String)
	 */
	@Override
	public List<Extension> getAllEnabledExtensions(String extensionPointId) {
		List<Extension> extensions = new ArrayList<Extension>();

        // first get all extensions from enabled apps
        for (AppDescriptor app : getAllEnabledApps()) {
            if (app.getExtensions() != null) {
                for (Extension candidate : app.getExtensions()) {
                    // extensions that belong to apps can't be disabled independently of their app, so we don't check AllComponentsState here
                    if (extensionPointId == null || extensionPointId.equals(candidate.getExtensionPointId())) {
                        extensions.addAll(app.getExtensions());
                    }
                }
            }
        }

        // now get "standalone extensions"
        for (Extension extension : allExtensions.getExtensions()) {
			if (extensionPointId == null || extensionPointId.equals(extension.getExtensionPointId())) {
                ComponentState state = allComponentsState.getComponentState(extension.getId(), ComponentType.EXTENSION);
                if (state == null || state.getEnabled()) {
    				extensions.add(extension);
                }
            }
		}
		
		return extensions;
	}
	
	@Override
	public void enableApp(String appId) {
		allComponentsState.setComponentState(appId, ComponentType.APP, true);
	}
	
	@Override
	public void disableApp(String appId) {
		allComponentsState.setComponentState(appId, ComponentType.APP, false);
	}
	
	@Override
	public void enableExtension(String extensionId) {
		allComponentsState.setComponentState(extensionId, ComponentType.EXTENSION, true);
	}
	
	@Override
	public void disableExtension(String extensionId) {
		allComponentsState.setComponentState(extensionId, ComponentType.EXTENSION, false);
	}
	
	@Override
	public List<Extension> getExtensionsForCurrentUser() {
		return getExtensionsForCurrentUser(null);
	}
	
	@Override
	public List<Extension> getExtensionsForCurrentUser(String extensionPointId) {
		List<Extension> extensions = new ArrayList<Extension>();
        UserContext userContext = Context.getUserContext();

        for (Extension candidate : getAllEnabledExtensions(extensionPointId)) {
            if ( (candidate.getBelongsTo() == null || hasPrivilege(userContext, candidate.getBelongsTo().getRequiredPrivilege()))
                    && hasPrivilege(userContext, candidate.getRequiredPrivilege()) ) {
                extensions.add(candidate);
            }
        }

		return extensions;
	}
	
	@Override
	public List<AppDescriptor> getAppsForCurrentUser() {
		List<AppDescriptor> userApps = new ArrayList<AppDescriptor>();
        UserContext userContext = Context.getUserContext();

		List<AppDescriptor> enabledApps = getAllEnabledApps();

		for (AppDescriptor candidate : enabledApps) {
            if (hasPrivilege(userContext, candidate.getRequiredPrivilege())) {
                userApps.add(candidate);
            }
		}
		return userApps;
	}

    @Override
    @Transactional(readOnly = true)
    public List<Location> getLoginLocations() {
        LocationTag supportsLogin = locationService.getLocationTagByName(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN);
        return locationService.getLocationsByTag(supportsLogin);
    }

    private boolean hasPrivilege(UserContext userContext, String privilege) {
        return StringUtils.isBlank(privilege) || userContext.hasPrivilege(privilege);
    }

    @Override
    public List<AppTemplate> getAllAppTemplates() {
        return allAppTemplates.getAppTemplates();
    }

    @Override
    public AppTemplate getAppTemplate(String id) {
        return allAppTemplates.getAppTemplate(id);
    }

    @Override
    public AppDescriptor getApp(String id) {
        return allAppDescriptors.getAppDescriptor(id);
    }

}
