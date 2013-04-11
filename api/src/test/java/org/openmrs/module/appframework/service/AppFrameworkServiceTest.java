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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

public class AppFrameworkServiceTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private AppFrameworkServiceImpl appFrameworkService;
	
	@Before
	public void setup() {
		//trigger loading of the apps
		new AppFrameworkActivator().contextRefreshed();
	}
	
	private User setupPrivilegesRolesAndUser(String privilegeToAssign) {
		UserService us = Context.getUserService();
		//Register the test privileges in the test *app.json and *extension.json files
		Privilege p1 = new Privilege("Some Random Privilege", "description1");
		us.savePrivilege(p1);
		
		Privilege p2 = new Privilege("Manage X-Rays", "description2");
		us.savePrivilege(p2);
		Privilege p3 = new Privilege("Another Random Ext Privilege", "description3");
		us.savePrivilege(p3);
		
		Role role = new Role("Test User", "description");
		if (p1.getPrivilege().equals(privilegeToAssign))
			role.addPrivilege(p1);
		else if (p2.getPrivilege().equals(privilegeToAssign))
			role.addPrivilege(p2);
		else if (p3.getPrivilege().equals(privilegeToAssign))
			role.addPrivilege(p3);
		
		us.saveRole(role);
		
		User u = new User();
		u.setPerson(new Person());
		u.addName(new PersonName("Some", null, "User"));
		u.setUsername("random");
		u.getPerson().setGender("M");
		u.addRole(role);
		
		return us.saveUser(u, "Openmr5xy");
	}
	
	/**
	 * @see {@link AppFrameworkService#getAllEnabledExtensions(String)}
	 */
	@Test
	@DirtiesContext
	@Verifies(value = "should get all extensions for the specified extensionPointId", method = "getAllEnabledExtensions(String)")
	public void getAllEnabledExtensions_shouldGetAllExtensionsForTheSpecifiedExtensionPointId() throws Exception {
		List<Extension> visitExts = appFrameworkService.getAllEnabledExtensions("activeVisitActions");
		assertEquals(1, visitExts.size());
		assertEquals("orderXrayExtension", visitExts.get(0).getId());
		
		List<Extension> patientLinkExts = appFrameworkService.getAllEnabledExtensions("patientLinks");
		assertEquals(1, patientLinkExts.size());
		assertEquals("gotoPatientExtension", patientLinkExts.get(0).getId());
	}
	
	/**
	 * @see {@link AppFrameworkService#getAppsForCurrentUser()}
	 */
	@Test
	@DirtiesContext
	@Verifies(value = "should get all enabled apps for the currently logged in user", method = "getAppsForCurrentUser()")
	public void getAppsForCurrentUser_shouldGetAllEnabledAppsForTheCurrentlyLoggedInUser() throws Exception {
		User user = setupPrivilegesRolesAndUser("Some Random Privilege");
		Context.authenticate(user.getUsername(), "Openmr5xy");
		assertEquals(user, Context.getAuthenticatedUser());
		
		List<AppDescriptor> userApps = appFrameworkService.getAppsForCurrentUser();
		assertEquals(2, userApps.size());//should include the app with that requires no privilege
		List<String> userAppIds = new ArrayList<String>();
		for (AppDescriptor app : userApps) {
			userAppIds.add(app.getId());
		}
		assertTrue(userAppIds.contains("patientDashboardApp"));
		assertTrue(userAppIds.contains("archiveRoomApp"));
	}
	
	/**
	 * @see {@link AppFrameworkService#getExtensionsForCurrentUser()}
	 */
	@Test
	@DirtiesContext
	@Verifies(value = "should get all enabled extensions for the currently logged in user", method = "getExtensionsForCurrentUser()")
	public void getExtensionsForCurrentUser_shouldGetAllEnabledExtensionsForTheCurrentlyLoggedInUser() throws Exception {
		User user = setupPrivilegesRolesAndUser("Some Random Privilege");
		Context.authenticate(user.getUsername(), "Openmr5xy");
		assertEquals(user, Context.getAuthenticatedUser());
		
		List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser();
		assertEquals(2, userExts.size());
		List<String> userAppIds = new ArrayList<String>();
		for (Extension ext : userExts) {
			userAppIds.add(ext.getId());
		}
		assertTrue(userAppIds.contains("orderXrayExtension"));
		assertTrue(userAppIds.contains("gotoArchives"));
	}
	
	/**
	 * @see {@link AppFrameworkService#getAppsForCurrentUser()}
	 */
	@Test
	@DirtiesContext
	@Verifies(value = "should return no app if there is no authenticated user", method = "getAppsForCurrentUser()")
	public void getAppsForCurrentUser_shouldReturnNoAppIfThereIsNoAuthenticatedUser() throws Exception {
		setupPrivilegesRolesAndUser("Some Random Privilege");
		if (Context.getAuthenticatedUser() != null)
			Context.logout();
		assertNull(Context.getAuthenticatedUser());
		assertTrue(appFrameworkService.getAppsForCurrentUser().isEmpty());
	}
	
	/**
	 * @see {@link AppFrameworkService#getExtensionsForCurrentUser()}
	 */
	@Test
	@DirtiesContext
	@Verifies(value = "should return no extension if there is no authenticated user", method = "getExtensionsForCurrentUser()")
	public void getExtensionsForCurrentUser_shouldReturnNoExtensionIfThereIsNoAuthenticatedUser() throws Exception {
		setupPrivilegesRolesAndUser("Some Random Privilege");
		if (Context.getAuthenticatedUser() != null)
			Context.logout();
		assertNull(Context.getAuthenticatedUser());
		assertTrue(appFrameworkService.getExtensionsForCurrentUser().isEmpty());
	}
}
