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

/**
 * A filter for login locations Instances of this interface will be invoked to determine whether a
 * login location should be included or excluded. Returning true by the {@link #accept(Location)}
 * method implies that a location should be included otherwise it will be excluded. Filters MUST be
 * registered as spring beans in order to get picked up. This qualifies as a FunctionalInterface, so
 * possibly when we upgrade the module to java 8, it should be annotated as one.
 * 
 * @since 2.14.0
 */
public interface LoginLocationFilter {
	
	/**
	 * Tests whether or not the specified location should be included among the login locations.
	 *
	 * @param location The location to be tested
	 * @return <code>true</code> if and only if <code>location</code> should be included
	 */
	boolean accept(Location location);
	
}
