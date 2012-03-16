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
package org.openmrs.module.appframework.fragment.controller;

import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;

/**
 * When you include this fragment in a page, then trying to view that page while not logged in will
 * send you back to the login page. To override this behavior, set the 'security.allowAnonymousBrowsing'
 * global property to 'true'. 
 * This fragment has no display component.
 */
public class MaybeRequireLoginFragmentController {
	
	public void controller() {
		if (!Context.isAuthenticated()) {
			boolean allowAnonymousBrowsing = false;
			try {
				String gp = Context.getAdministrationService().getGlobalProperty("security.allowAnonymousBrowsing");
				allowAnonymousBrowsing = Boolean.valueOf(gp);
			}
			catch (Exception ex) {}
			if (!allowAnonymousBrowsing)
				throw new APIAuthenticationException("Login is required");
		}
	}
	
}
