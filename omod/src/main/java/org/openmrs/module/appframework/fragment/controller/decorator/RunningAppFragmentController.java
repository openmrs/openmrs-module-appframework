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
package org.openmrs.module.appframework.fragment.controller.decorator;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;


/**
 *
 */
public class RunningAppFragmentController {

	public void controller(FragmentConfiguration config, FragmentModel model) {
		String appId = (String) config.getAttribute("appId");
		if (appId != null) {
			AppDescriptor app = Context.getService(AppFrameworkService.class).getAppById(appId);
			model.addAttribute("app", app);
		} else {
			model.addAttribute("app", null);
		}
	}
	
}
