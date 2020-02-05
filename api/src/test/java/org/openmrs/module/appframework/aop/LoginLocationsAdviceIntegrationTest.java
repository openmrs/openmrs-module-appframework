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
package org.openmrs.module.appframework.aop;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginLocationsAdviceIntegrationTest extends BaseModuleContextSensitiveTest {
	
	private LoginLocationsAdvice loginLocationsAdvice;
	
	private Location loginLocation1;
	
	private Location loginLocation2;

    private Location otherLocation;
    
    private LocationTag loginLocationTag;
    
    private LocationTag otherLocationTag;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private AppFrameworkService appFrameworkService;
	
	@Before
    public void setUp() throws Exception {
		loginLocationsAdvice = new LoginLocationsAdvice();
		loginLocation1 = new Location();
		loginLocation2 = new Location();
        otherLocation = new Location();
        
        loginLocationTag = new LocationTag();
        otherLocationTag = new LocationTag();
        loginLocationTag.setUuid(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN_UUID);
        loginLocationTag.setName(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN);
        otherLocationTag.setUuid("other-location-tag-uuid");
        otherLocationTag.setName("Other Location Tag");
        
        locationService.saveLocationTag(loginLocationTag);
        locationService.saveLocationTag(otherLocationTag);
        
        loginLocation1.setUuid("loginLocation1_uuid");
        loginLocation1.setName("loginLocation1");
        loginLocation1.setTags(Collections.singleton(loginLocationTag));
        
        loginLocation2.setUuid("loginLocation2_uuid");
        loginLocation2.setName("loginLocation2");
        loginLocation2.setTags(Collections.singleton(loginLocationTag));

        otherLocation.setUuid("loginLocation3_uuid");
        otherLocation.setName("loginLocation3");
        otherLocation.setTags(Collections.singleton(otherLocationTag));
        
        Context.addAdvice(LocationService.class, loginLocationsAdvice);
    }
	
	@Test
    public void testAfterReturning_shouldUpdateCachedLoginLocationsGivenSaveLocationCalled () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation1);
		
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		
		// replay
		locationService.saveLocation(loginLocation2);
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 2);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getUuid(), "loginLocation2_uuid");
    }
	
	@Test
    public void testAfterReturning_shouldUpdateCachedLoginLocationsGivenRetiredLocationCalled () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation1);
		locationService.saveLocation(loginLocation2);
		assertEquals(appFrameworkService.getLoginLocations().size(), 2);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getUuid(), "loginLocation2_uuid");

		// replay
		locationService.retireLocation(loginLocation2, "some reason");
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		
    }
	
	@Test
    public void testAfterReturning_shouldUpdateCachedLoginLocationsGivenUnretiredLocationCalled () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation1);
		locationService.saveLocation(loginLocation2);
		locationService.retireLocation(loginLocation2, "some reason");
		
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");

		// replay
		locationService.unretireLocation(loginLocation2);
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 2);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getUuid(), "loginLocation2_uuid");
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenOtherMethodCalled () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation1);
		locationService.saveLocation(loginLocation2);
		
		assertEquals(appFrameworkService.getLoginLocations().size(), 2);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getUuid(), "loginLocation2_uuid");
		
		// replay
		locationService.getAllLocations();
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 2);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(1).getUuid(), "loginLocation2_uuid");

    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenNonLoginLocationSaved () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation2);
		
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation2_uuid");
		
		// replay
		locationService.saveLocation(otherLocation);
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation2_uuid");
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenNonLoginLocationRetired () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation2);
		
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation2_uuid");
		
		// replay
		locationService.retireLocation(otherLocation, "some reason");
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation2");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation2_uuid");
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenNonLoginLocationUnretired () throws Throwable {
		// setup
		locationService.saveLocation(loginLocation1);
		
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
		
		// replay
		locationService.unretireLocation(otherLocation);
		
		// verify
		assertEquals(appFrameworkService.getLoginLocations().size(), 1);
		assertEquals(appFrameworkService.getLoginLocations().get(0).getName(), "loginLocation1");
		assertEquals(appFrameworkService.getLoginLocations().get(0).getUuid(), "loginLocation1_uuid");
    }
}