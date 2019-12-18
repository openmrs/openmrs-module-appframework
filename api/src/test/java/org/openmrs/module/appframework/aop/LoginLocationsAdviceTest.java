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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.context.Context;
import org.openmrs.api.LocationService;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.repository.AllLoginLocations;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class LoginLocationsAdviceTest {
	
	private LoginLocationsAdvice loginLocationsAdvice;
	
	private AllLoginLocations allLoginLocations;
	
	private Location loginLocation1;
	
	private Location loginLocation2;

    private Location otherLocation;
    
    private LocationTag loginLocationTag;
    
    private LocationTag otherLocationTag;
    
    private AppFrameworkService appFrameworkService;
	
	@Before
    public void setUp() throws Exception {
		loginLocationsAdvice = new LoginLocationsAdvice();
		allLoginLocations = new AllLoginLocations();
		loginLocation1 = new Location();
		loginLocation2 = new Location();
        otherLocation = new Location();
        
        loginLocationTag = new LocationTag();
        otherLocationTag = new LocationTag();
        loginLocationTag.setUuid(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN_UUID);
        otherLocationTag.setUuid("some-location-tag-uuid");
        
        loginLocation1.setId(1);
        loginLocation1.setUuid("loginloginLocation1_uuid");
        loginLocation1.setName("loginloginLocation1");
        loginLocation1.setTags(Collections.singleton(loginLocationTag));
        
        loginLocation2.setId(2);
        loginLocation2.setUuid("loginloginLocation2_uuid");
        loginLocation2.setName("loginloginLocation2");
        loginLocation2.setTags(Collections.singleton(loginLocationTag));

        otherLocation.setId(3);
        otherLocation.setUuid("loginLocation3_uuid");
        otherLocation.setName("loginLocation3");
        otherLocation.setTags(Collections.singleton(otherLocationTag));
        
        allLoginLocations.add(loginLocation1);
        allLoginLocations.add(otherLocation);
        
        appFrameworkService = mock(AppFrameworkService.class);
		PowerMockito.mockStatic(Context.class);
		
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(loginLocation1));
        when(Context.getRegisteredComponent(eq("appFrameworkService"), eq(AppFrameworkService.class))).thenReturn(appFrameworkService);
        when(Context.getRegisteredComponents(eq(AllLoginLocations.class))).thenReturn(Arrays.asList(allLoginLocations));

    }
	
	@Test
    public void testAfterReturning_shouldUpdateCachedLoginLocationsGivenSaveLocationCalled () throws Throwable {
		// setup
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(loginLocation1));
		
		// replay
		loginLocationsAdvice.afterReturning(loginLocation1, LocationService.class.getDeclaredMethod("saveLocation", Location.class), null, null);
		
		// verify
		verify(appFrameworkService, times(1)).getLoginLocations();
		assertEquals(allLoginLocations.getLoginLocations().size(), 1);
		assertEquals(allLoginLocations.getLoginLocations().get(0).getName(), loginLocation1.getName());
		assertEquals(allLoginLocations.getLoginLocations().get(0).getUuid(), loginLocation1.getUuid());
    }
	
	@Test
    public void testAfterReturning_shouldUpdateCachedLoginLocationsGivenRetiredLocationCalled () throws Throwable {
		// setup
		loginLocation1.setRetired(true);
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(loginLocation2));
		
		// replay
		loginLocationsAdvice.afterReturning(loginLocation1, LocationService.class.getDeclaredMethod("retireLocation", Location.class, String.class), null, null);
		
		// verify
		verify(appFrameworkService, times(1)).getLoginLocations();
		assertEquals(allLoginLocations.getLoginLocations().size(), 1);
		assertEquals(allLoginLocations.getLoginLocations().get(0).getName(), loginLocation2.getName());
		assertEquals(allLoginLocations.getLoginLocations().get(0).getUuid(), loginLocation2.getUuid());
    }
	
	@Test
    public void testAfterReturning_shouldUpdateCachedLoginLocationsGivenUnretiredLocationCalled () throws Throwable {
		// setup
		loginLocation1.setRetired(false);
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(loginLocation1, loginLocation2));
		
		// replay
		loginLocationsAdvice.afterReturning(loginLocation1, LocationService.class.getDeclaredMethod("unretireLocation", Location.class), null, null);
		
		// verify
		verify(appFrameworkService, times(1)).getLoginLocations();
		assertEquals(allLoginLocations.getLoginLocations().size(), 2);
		assertEquals(allLoginLocations.getLoginLocations().get(0).getName(), loginLocation1.getName());
		assertEquals(allLoginLocations.getLoginLocations().get(0).getUuid(), loginLocation1.getUuid());
		assertEquals(allLoginLocations.getLoginLocations().get(1).getName(), loginLocation2.getName());
		assertEquals(allLoginLocations.getLoginLocations().get(1).getUuid(), loginLocation2.getUuid());
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenOtherMethodCalled () throws Throwable {
		// setup
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(loginLocation1));
		
		// replay
		loginLocationsAdvice.afterReturning(loginLocation1, LocationService.class.getDeclaredMethod("getAllLocations"), null, null);
		
		// verify
		verify(appFrameworkService, times(0)).getLoginLocations();
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenNonLoginLocationSaved () throws Throwable {
		// setup
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(otherLocation));
		
		// replay
		loginLocationsAdvice.afterReturning(otherLocation, LocationService.class.getDeclaredMethod("saveLocation", Location.class), null, null);
		
		// verify
		verify(appFrameworkService, times(0)).getLoginLocations();
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenNonLoginLocationRetired () throws Throwable {
		// setup
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(otherLocation));
		
		// replay
		loginLocationsAdvice.afterReturning(otherLocation, LocationService.class.getDeclaredMethod("retireLocation", Location.class, String.class), null, null);
		
		// verify
		verify(appFrameworkService, times(0)).getLoginLocations();
    }
	
	@Test
    public void testAfterReturning_shouldNotUpdateCachedLoginLocationsGivenNonLoginLocationUnretired () throws Throwable {
		// setup
		when(appFrameworkService.getLoginLocations()).thenReturn(Arrays.asList(otherLocation));
		
		// replay
		loginLocationsAdvice.afterReturning(otherLocation, LocationService.class.getDeclaredMethod("unretireLocation", Location.class), null, null);
		
		// verify
		verify(appFrameworkService, times(0)).getLoginLocations();
    }
}