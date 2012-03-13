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

import org.openmrs.module.web.extension.PortletExt;


/**
 * Shows the Choose-an-App homepage for OpenMRS 1.x
 */
public class HomepageExtension extends PortletExt {

	/**
	 * @see PortletExt#getPortletUrl()
	 */
	@Override
    public String getPortletUrl() {
		return "appFrameworkHomepage";
	}
	
	/**
	 * @see PortletExt#getPortletParameters()
	 */
	@Override
    public String getPortletParameters() {
		return "";
	}
}