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
package org.openmrs.module.appframework.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

public class AppFrameworkServiceTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private AppFrameworkServiceImpl appFrameworkService;
	
	/**
	 * @see {@link AppFrameworkService#getAllEnabledExtensions(String)}
	 */
	@Test
	@DirtiesContext
	@Verifies(value = "should get all extensions for the specified extensionPointId", method = "getAllEnabledExtensions(String)")
	public void getAllEnabledExtensions_shouldGetAllExtensionsForTheSpecifiedExtensionPointId() throws Exception {
		//trigger loading of the apps
		new AppFrameworkActivator().contextRefreshed();
		
		List<Extension> visitExts = appFrameworkService.getAllEnabledExtensions("activeVisitActions");
		assertEquals(1, visitExts.size());
		assertEquals("orderXrayExtension", visitExts.get(0).getId());
		
		List<Extension> patientLinkExts = appFrameworkService.getAllEnabledExtensions("patientLinks");
		assertEquals(1, patientLinkExts.size());
		assertEquals("gotoPatientExtension", patientLinkExts.get(0).getId());
	}
}
