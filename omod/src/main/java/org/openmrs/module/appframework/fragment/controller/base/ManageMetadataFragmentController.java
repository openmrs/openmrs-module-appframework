package org.openmrs.module.appframework.fragment.controller.base;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.openmrs.OpenmrsMetadata;
import org.openmrs.module.appframework.fragment.controller.base.helper.PropertyConfiguration;
import org.openmrs.ui.framework.fragment.FragmentActionRequest;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.validation.Errors;

/**
 * Helper class for doing manage-metadata fragments
 * @param <T> the type of metadata you want to manage
 */
public abstract class ManageMetadataFragmentController<T extends OpenmrsMetadata> {
	
	public String controller(FragmentModel model, FragmentConfiguration configuration) {
		String mode = (String) configuration.getAttribute("mode");
		if (mode == null)
			mode = "list";
		if (mode.equals("list")) {
			return listController(model, configuration);
		} else if (mode.equals("edit")) {
			return editController(model, configuration);
		} else {
			throw new RuntimeException("unkown mode: " + mode);
		}
	}
	
	// support for listing all items
	
	private String listController(FragmentModel model, FragmentConfiguration configuration) {
		List<T> allMetadata = getAllMetadata();
		
		// just in case some core service methods don't put retired last
		Collections.sort(allMetadata, new Comparator<T>() {
			
			@Override
			public int compare(T left, T right) {
				int temp = left.isRetired().compareTo(right.isRetired());
				if (temp == 0)
					temp = left.getName().compareTo(right.getName());
				return temp;
			}
		});
		
		model.addAttribute("uniqueIdProperty", getUniqueIdProperty());
		model.addAttribute("allMetadata", allMetadata);
		model.addAttribute("propertiesToDisplay", getPropertiesToDisplay());
		return getListViewName();
	}
	
	protected String getListViewName() {
		return "listMetadata";
	}
	
	protected Map<String, String> getPropertiesToDisplay() {
		Map<String, String> ret = new LinkedHashMap<String, String>();
		ret.put("name", "Name");
		ret.put("description", "Description");
		return ret;
	}
	
	protected abstract List<T> getAllMetadata();
	
	// support for creating/editing a single item
	
	private String editController(FragmentModel model, FragmentConfiguration configuration) {
		String id = (String) configuration.getAttribute("uniqueId");
		T metadata = id == null ? createNew() : getExisting(id);
		
		PropertyConfiguration propConfig = new PropertyConfiguration();
		configureEditableProperties(propConfig);
		
		model.addAttribute("uniqueIdProperty", getUniqueIdProperty());
		model.addAttribute("metadata", metadata);
		model.addAttribute("titleCode", getTitleCode());
		model.addAttribute("propertiesToEdit", getPropertiesToEdit());
		model.addAttribute("propertyConfiguration", propConfig);
		
		return getEditViewName();
	}
	
	protected Map<String, String> getPropertiesToEdit() {
		Map<String, String> ret = new LinkedHashMap<String, String>();
		// TODO BW: Use translatable strings here instead?  Or else Name/Description/Parent can be the keys I guess
		ret.put("name", "Name");
		ret.put("description", "Description");
		return ret;
	}
	
	protected void configureEditableProperties(PropertyConfiguration propertyConfiguration) {
		propertyConfiguration.add("description", "type", "textarea");
		propertyConfiguration.add("description", "rows", 5);
		propertyConfiguration.add("description", "cols", 60);
	}
	
	public Object save(FragmentActionRequest request) {
		T newObject = createNew();
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) newObject.getClass();
		T metadata = request.getParameter("id", clazz);
		if (metadata == null)
			metadata = newObject;
		
		for (Map.Entry<String, String> prop : getPropertiesToEdit().entrySet()) {
			try {
				String propName = prop.getKey();
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(metadata, propName);
				Class<?> propType = pd.getPropertyType();
				Object propVal = request.getParameter(propName, propType);
				pd.getWriteMethod().invoke(metadata, propVal);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error setting property: " + prop, ex);
			}
		}
		
		validate(metadata, request.getErrors());
		
		if (request.hasErrors()) {
			return request.getErrors();
		} else {
			saveMetadata(metadata);
			return null;
		}
	}
	
	protected void validate(T metadata, Errors errors) {
	}
	
	public void retire(FragmentActionRequest request) {
		T newObject = createNew();
		Class<T> clazz = (Class<T>) newObject.getClass();
		T metadata = request.getRequiredParameter("id", clazz, "id");
		String reason = request.getParameter("reason", String.class);
		if (reason == null)
			reason = "UI";
		retireMetadata(metadata, reason);
	}
	
	public void unretire(FragmentActionRequest request) {
		T newObject = createNew();
		Class<T> clazz = (Class<T>) newObject.getClass();
		T metadata = request.getRequiredParameter("id", clazz, "id");
		unretireMetadata(metadata);
	}
	
	protected String getTitleCode() {
		return "Metadata";
	}
	
	protected String getEditViewName() {
		return "editMetadata";
	}
	
	protected abstract T createNew();
	
	protected abstract T getExisting(String uniqueId);
	
	protected abstract void saveMetadata(T metadata);
	
	protected abstract void retireMetadata(T metadata, String reason);
	
	protected abstract void unretireMetadata(T metadata);
	
	public String getUniqueIdProperty() {
		return "id";
	}
	
}
