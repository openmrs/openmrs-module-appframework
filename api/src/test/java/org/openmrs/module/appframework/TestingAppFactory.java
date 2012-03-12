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

import java.util.ArrayList;
import java.util.List;


/**
 * For use in test cases
 */
public class TestingAppFactory implements AppFactory {
	
	/**
	 * @see org.openmrs.module.appframework.AppFactory#getAppDescriptors()
	 */
	@Override
	public List<AppDescriptor> getAppDescriptors() {
		List<AppDescriptor> ret = new ArrayList<AppDescriptor>();
		ret.add(new SimpleAppDescriptor("dataentry", "Data Entry"));
		ret.add(new SimpleAppDescriptor("charting", "Charting"));
		return ret;
	}
	
}
