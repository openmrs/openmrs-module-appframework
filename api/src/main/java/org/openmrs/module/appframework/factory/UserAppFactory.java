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
package org.openmrs.module.appframework.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.repository.AllUserApps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAppFactory implements AppFrameworkFactory {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private AllUserApps allUserApps;
	
	@Autowired
	public UserAppFactory(AllUserApps allUserApps) {
		this.allUserApps = allUserApps;
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	}
	
	@Override
	public List<AppDescriptor> getAppDescriptors() throws IOException {
		List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();
		List<UserApp> userApps = allUserApps.getUserApps();
		for (UserApp userApp : userApps) {
			try {
				AppDescriptor appDescriptor = objectMapper.readValue(userApp.getJson(), AppDescriptor.class);
				appDescriptors.add(appDescriptor);
			}
			catch (IOException e) {
				log.fatal("Error reading app user app json " + userApp.getAppId(), e);
			}
		}
		return appDescriptors;
	}
	
	@Override
	public List<Extension> getExtensions() throws IOException {
		return null;
	}
	
	@Override
	public List<AppTemplate> getAppTemplates() throws IOException {
		return null;
	}
}
