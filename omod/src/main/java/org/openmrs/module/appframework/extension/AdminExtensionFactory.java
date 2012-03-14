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
package org.openmrs.module.appframework.extension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.ui.framework.extension.Extension;
import org.openmrs.ui.framework.extension.ExtensionFactory;
import org.openmrs.ui.framework.extension.ExtensionPoint;
import org.openmrs.ui.framework.extension.ExtensionPointFactory;
import org.openmrs.ui.framework.extension.LinkExtension;
import org.springframework.stereotype.Component;


/**
 * Extensions for the Configure Metadata page
 */
@Component
public class AdminExtensionFactory implements ExtensionFactory, ExtensionPointFactory {
	
	/**
	 * @see org.openmrs.ui.framework.extension.ExtensionPointFactory#getExtensionPoints()
	 */
	@Override
	public List<ExtensionPoint> getExtensionPoints() {
		List<ExtensionPoint> ret = new ArrayList<ExtensionPoint>();

		ret.add(new ExtensionPoint("admin.manageMetadata", "manageMetadata.extensionPoint.description", LinkExtension.class));
		
		return ret;
	}
	
	/**
	 * @see org.openmrs.ui.framework.extension.ExtensionFactory#getExtensions()
	 */
	@Override
	public Map<String, Extension> getExtensions() {
		Map<String, Extension> ret = new LinkedHashMap<String, Extension>();
		
		ret.put("admin.manageMetadata.administerLocations", new LinkExtension("admin.manageMetadata", "Locations",
	        "fragment:administerLocations", "globe_32.png", "Manage Locations"));
		
		return ret;
	}
	
}
