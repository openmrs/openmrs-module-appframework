package org.openmrs.module.appframework.fragment.controller.base.helper;

import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyConfiguration extends LinkedHashMap<String, Map<String, Object>> {
	
	private static final long serialVersionUID = 1L;
	
	public void add(String property, String setting, Object value) {
		Map<String, Object> forProperty = get(property);
		if (forProperty == null) {
			forProperty = new LinkedHashMap<String, Object>();
			put(property, forProperty);
		}
		forProperty.put(setting, value);
	}
}
