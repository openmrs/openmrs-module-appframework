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
package org.openmrs.module.appframework.fragment.controller.widget;

import org.apache.commons.beanutils.PropertyUtils;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.fragment.FragmentRequest;

public class FieldFragmentController {
	
	public Object controller(FragmentConfiguration config, FragmentModel model) throws Exception {
		config.require("value | class | object"); // should be "value | class | (object & property)" but this isn't supported yet 
		
		if (config.getAttribute("value") != null) {
			model.addAttribute("object", config.getAttribute("value"));
			return "formatObject";
		}
		
		Class<?> clazz;
		Object currentValue = null;
		if (config.getAttribute("object") != null) {
			config.require("property"); // remove this when the above require can check for it
			Object bean = config.getAttribute("object");
			String property = (String) config.getAttribute("property");
			clazz = PropertyUtils.getPropertyType(bean, property);
			currentValue = PropertyUtils.getProperty(bean, property);
		} else {
			Object classProp = config.getAttribute("class");
			if (classProp instanceof Class<?>) {
				clazz = (Class<?>) classProp;
			} else {
				clazz = Context.loadClass(classProp.toString());
			}
		}
		
		// add a few extra config properties for the fragment we forward to 
		config.addAttribute("propertyClass", clazz);
		if (config.getAttribute("currentValue") == null)
			config.addAttribute("currentValue", currentValue);
		
		// by convention these are under 
		return new FragmentRequest("field/" + clazz.getName(), config);
	}
	
}