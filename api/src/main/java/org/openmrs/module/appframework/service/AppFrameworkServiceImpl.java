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

import java.util.*;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * It is a default implementation of {@link AppFrameworkService}.
 */
@Service
public class AppFrameworkServiceImpl extends BaseOpenmrsService implements AppFrameworkService {

    private AllAppDescriptors allAppDescriptors;

    private AllExtensions allExtensions;

    @Autowired
    public AppFrameworkServiceImpl(AllAppDescriptors allAppDescriptors, AllExtensions allExtensions) {
        this.allAppDescriptors = allAppDescriptors;
        this.allExtensions = allExtensions;
    }

    @Override
    public List<AppDescriptor> getAllApps() {
        return allAppDescriptors.getAppDescriptors();
    }

    @Override
    public List<Extension> getAllExtensions(String appId, String extensionPointId) {
        List<Extension> extensions = allExtensions.getExtensions();
        List<Extension> matchingExtensions = new ArrayList<Extension>();
        for(Extension extension : extensions) {
            if (extension.getAppId().equalsIgnoreCase(appId) &&
                extension.getExtensionPointId().equalsIgnoreCase(extensionPointId))
                matchingExtensions.add(extension);
        }
        return matchingExtensions;
    }
}