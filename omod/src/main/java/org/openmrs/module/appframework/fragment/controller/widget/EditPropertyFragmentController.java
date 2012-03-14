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

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentRequest;

public class EditPropertyFragmentController {
	
	public Object controller(FragmentConfiguration configuration) throws Exception {
		Object bean = configuration.getAttribute("bean");
		String propertyName = (String) configuration.getAttribute("property");
		if (bean == null || propertyName == null)
			throw new RuntimeException("'bean' and 'property' are required");
		
		PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
		Class<?> propertyClass = descriptor.getPropertyType();
		Object currentValue = PropertyUtils.getProperty(bean, propertyName);
		
		// add a few extra config properties for the fragment we forward to 
		configuration.addAttribute("formFieldName", propertyName);
		configuration.addAttribute("class", propertyClass);
		configuration.addAttribute("currentValue", currentValue);
		
		return new FragmentRequest("widget/field", configuration);
	}
	
}