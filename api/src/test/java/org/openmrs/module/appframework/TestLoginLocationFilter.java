/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.appframework;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.module.appframework.service.AppFrameworkServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestLoginLocationFilter implements LoginLocationFilter {
	
	@Autowired
	private LocationService locationService;
	
	private boolean isEnabled = false;
	
	@Override
	public boolean accept(Location location) {
		if (!isEnabled) {
			return true;
		}
		return AppFrameworkServiceTest.LOCATION_UUID1.equals(location.getUuid())
		        || AppFrameworkServiceTest.LOCATION_UUID2.equals(location.getUuid());
	}
	
	public void enable() {
		isEnabled = true;
	}
	
}
