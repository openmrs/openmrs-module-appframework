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
package org.openmrs.module.appframework.aop;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.repository.AllLoginLocations;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.springframework.aop.AfterReturningAdvice;

public class LoginLocationsAdvice implements AfterReturningAdvice {
    
    private static final Log log = LogFactory.getLog(LoginLocationsAdvice.class);
    private static Set<String> loginLocationsUpdateTriggers = new HashSet<String>();

    static {
        loginLocationsUpdateTriggers.add("saveLocation");
        loginLocationsUpdateTriggers.add("retireLocation");
        loginLocationsUpdateTriggers.add("unretireLocation");
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if(!loginLocationsUpdateTriggers.contains(method.getName())) {
            return;
        }
        Set<LocationTag> tags = ((Location) returnValue).getTags();
        for (Iterator<LocationTag> tagsIterator = tags.iterator(); tagsIterator.hasNext(); ) {
            if(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN_UUID.equals(tagsIterator.next().getUuid())) {
                // The login locations are first set in AppFrameworkActivator
                AllLoginLocations allLoginLocations = Context.getRegisteredComponents(AllLoginLocations.class).get(0);
                allLoginLocations.clear();
                allLoginLocations.add(((AppFrameworkService) Context.getRegisteredComponent("appFrameworkService", AppFrameworkService.class)).getLoginLocations());
                return;
            }
        }
    }
}