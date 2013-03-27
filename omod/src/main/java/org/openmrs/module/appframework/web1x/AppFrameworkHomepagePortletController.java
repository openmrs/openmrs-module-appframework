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
package org.openmrs.module.appframework.web1x;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.controller.PortletController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class AppFrameworkHomepagePortletController extends PortletController {
	
	/**
	 * @see org.openmrs.web.controller.PortletController#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User currentUser = Context.getAuthenticatedUser();
		
		String[] appParam = req.getParameterValues("app[]");
		if (appParam != null) {
			StringBuilder data = new StringBuilder();
			for (String app : appParam) {
				data.append(app).append(",");
			}
			
			// save the sort order to the user's properties
			log.debug("order: " + data.toString());

			try {
				Context.addProxyPrivilege(PrivilegeConstants.EDIT_USERS);
				Context.getUserService().saveUser(currentUser, null);
			}
			finally {
				Context.removeProxyPrivilege(PrivilegeConstants.EDIT_USERS);
			}
		}
		
		AppFrameworkService service = Context.getService(AppFrameworkService.class);

//		List<AppDescriptor> apps = service.getAppsForUser(currentUser);
		List<AppDescriptor> apps = service.getAllApps();

		if (apps.size() == 0) {
			// since I haven't implemented a UI for enabling apps yet, show all apps for testing:
			apps.addAll(service.getAllApps());
			// after we've implemented enabling apps, switch to this:
			//apps.add(service.getAppById("legacy.openmrs"));
		}

		// if there's only one app enabled for this user, go straight to its homepage
		/* TODO: Figure out how to get this working. Maybe we're not allowed to redirect in a portlet?
		if (apps.size() == 1) {
			String url = apps.get(0).getHomepageUrl();
			try {
				// this line will throw an exception if it's not a full url 
				new URL(url);
				// this is a full url including protocol, so we use it verbatim
				return new ModelAndView(new RedirectView(url));
			}
			catch (Exception ex) {
				// no protocol, so we interpret this relative to the webapp root
				return new ModelAndView(new RedirectView(url, true));
			}
		}
		*/
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apps", apps);
		
		return new ModelAndView("module/appframework/appFrameworkHomepage", model);
	}
	
}
