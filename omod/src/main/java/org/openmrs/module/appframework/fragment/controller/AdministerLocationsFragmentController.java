package org.openmrs.module.appframework.fragment.controller;

import java.util.List;
import java.util.Map;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.fragment.controller.base.ManageMetadataFragmentController;
import org.openmrs.validator.LocationValidator;
import org.springframework.validation.Errors;

public class AdministerLocationsFragmentController extends ManageMetadataFragmentController<Location> {
	
	@Override
	protected List<Location> getAllMetadata() {
		return Context.getLocationService().getAllLocations();
	}
	
	@Override
	protected Map<String, String> getPropertiesToDisplay() {
		Map<String, String> ret = super.getPropertiesToDisplay();
		ret.put("parentLocation", "Parent");
		return ret;
	}
	
	@Override
	protected Map<String, String> getPropertiesToEdit() {
		Map<String, String> ret = super.getPropertiesToDisplay();
		ret.put("parentLocation", "Parent");
		return ret;
	}
	
	@Override
	protected Location createNew() {
		return new Location();
	}
	
	@Override
	protected Location getExisting(String uniqueId) {
		return Context.getLocationService().getLocation(Integer.valueOf(uniqueId));
	}
	
	@Override
	protected void validate(Location metadata, Errors errors) {
		new LocationValidator().validate(metadata, errors);
	}
	
	@Override
	protected void saveMetadata(Location metadata) {
		Context.getLocationService().saveLocation(metadata);
	}
	
	@Override
	protected void retireMetadata(Location metadata, String reason) {
		Context.getLocationService().retireLocation(metadata, reason);
	}
	
	@Override
	protected void unretireMetadata(Location metadata) {
		Context.getLocationService().unretireLocation(metadata);
	}
	
	@Override
	protected String getTitleCode() {
		return "Location";
	}
}
