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
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.openmrs.util.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@DirtiesContext
public class AppFrameworkServiceTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private AppFrameworkService appFrameworkService;
	
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
        Privilege p4 = new Privilege("Register Patients", "description4");
        us.savePrivilege(p4);

        Role role = new Role("Test User", "description");
		if (p1.getPrivilege().equals(privilegeToAssign))
			role.addPrivilege(p1);
		else if (p2.getPrivilege().equals(privilegeToAssign))
			role.addPrivilege(p2);
		else if (p3.getPrivilege().equals(privilegeToAssign))
			role.addPrivilege(p3);
        else if (p4.getPrivilege().equals(privilegeToAssign))
            role.addPrivilege(p4);

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

    @Test
    public void testgetExtensionsForCurrentUser_shouldGetExtensionsBasedOnPrivilegeCheckOnOwningApp() throws Exception {
        User user = setupPrivilegesRolesAndUser("Register Patients");
        Context.authenticate(user.getUsername(), "Openmr5xy");
        assertEquals(user, Context.getAuthenticatedUser());

        List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser("homepageLink");
        assertThat(userExts.size(), is(1));
        assertThat(userExts.get(0).getId(), is("registerOutpatientHomepageLink"));
    }

    /**
	 * @see {@link AppFrameworkService#getAppsForCurrentUser()}
	 */
	@Test
	@Verifies(value = "should return apps with no required privilege if there is no authenticated user", method = "getAppsForCurrentUser()")
	public void getAppsForCurrentUser_shouldReturnNoAppIfThereIsNoAuthenticatedUser() throws Exception {
		setupPrivilegesRolesAndUser("Some Random Privilege");
		if (Context.getAuthenticatedUser() != null)
			Context.logout();
		assertNull(Context.getAuthenticatedUser());
        List<AppDescriptor> actual = appFrameworkService.getAppsForCurrentUser();
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getId(), is("archiveRoomApp")); // this requires no privilege
    }
	
	/**
	 * @see {@link AppFrameworkService#getExtensionsForCurrentUser()}
	 */
	@Test
	@Verifies(value = "should return extensions with no required privilege if there is no authenticated user", method = "getExtensionsForCurrentUser()")
	public void getExtensionsForCurrentUser_shouldReturnExtensionsWithNoRequiredPrivilegeIfThereIsNoAuthenticatedUser() throws Exception {
		setupPrivilegesRolesAndUser("Some Random Privilege");
		if (Context.getAuthenticatedUser() != null)
			Context.logout();
		assertNull(Context.getAuthenticatedUser());
		assertThat(appFrameworkService.getExtensionsForCurrentUser(), hasSize(1));
        assertThat(appFrameworkService.getExtensionsForCurrentUser().get(0).getId(), is("gotoArchives"));
	}
	
	/**
	 * @see AppFrameworkService#getExtensionsForCurrentUser(String)
	 * @verifies get all enabled extensions for the logged in user and extensionPointId
	 */
	@Test
	@Verifies(value = "should return no extension if there is no authenticated user", method = "getExtensionsForCurrentUser(String)")
	public void getExtensionsForCurrentUser_shouldReturnNoExtensionForNoLoggedInUser()
	    throws Exception {
        Context.logout();
		List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser("activeVisitActions");
		assertEquals(0, userExts.size());
	}

    /**
     * @see AppFrameworkService#getExtensionsForCurrentUser(String)
     * @verifies get all enabled extensions for the logged in user and extensionPointId
     */
    @Test
    @Verifies(value = "should get all enabled extensions for the logged in user and extension point id", method = "getExtensionsForCurrentUser(String)")
    public void getExtensionsForCurrentUser_shouldGetAllEnabledExtensionsForTheLoggedInUserAndExtensionPointId()
        throws Exception {
        User user = setupPrivilegesRolesAndUser("Some Random Privilege");
        Context.authenticate(user.getUsername(), "Openmr5xy");
        assertEquals(user, Context.getAuthenticatedUser());

        List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser("activeVisitActions");
        assertEquals(1, userExts.size());
        assertEquals("orderXrayExtension", userExts.get(0).getId());
    }

    /**
     * @see AppFrameworkService#getExtensionsForCurrentUser(String)
     * @verifies get all enabled extensions for the logged in user and extensionPointId
     */
    @Test
    @Verifies(value = "should return no extension for is user does not have privilege", method = "getExtensionsForCurrentUser(String)")
    public void getExtensionsForCurrentUser_shouldReturnNoExtensionForUserWithoutPrivilege()
        throws Exception {
        User user = setupPrivilegesRolesAndUser("Another Random Ext Privilege");
        Context.authenticate(user.getUsername(), "Openmr5xy");
        assertEquals(user, Context.getAuthenticatedUser());

        List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser("activeVisitActions");
        assertEquals(0, userExts.size());
    }

    /**
     * @see AppFrameworkService#getExtensionsForCurrentUser(String)
     * @verifies get all enabled extensions for the logged in user and extensionPointId
     */
    @Test
    @Verifies(value = "should return just extensions of the id for super user", method = "getExtensionsForCurrentUser(String)")
    public void getExtensionsForCurrentUser_shouldGetAllEnabledExtensionsForTheSuperUserAndExtensionPointId()
        throws Exception {

        User user = new User();
        user.setPerson(new Person());
        user.setUsername("username");
        user.addName(new PersonName("Some", null, "User"));
        user.getPerson().setGender("M");
        user.addRole(new Role(RoleConstants.SUPERUSER, "description"));

        UserService us = Context.getUserService();
        us.saveUser(user, "Openmr5xy");

        Context.authenticate(user.getUsername(), "Openmr5xy");
        assertEquals(user, Context.getAuthenticatedUser());

        List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser("activeVisitActions");
        assertEquals(1, userExts.size());
        assertEquals("orderXrayExtension", userExts.get(0).getId());
    }

    @Test
    public void getAllAppTemplates_shouldGetAppTemplates() throws Exception {
        List<AppTemplate> actual = appFrameworkService.getAllAppTemplates();
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0).getId(), is("registrationapp.registerPatient"));
    }

    @Test
    public void getAppTemplateById_shouldGetAppTemplate() throws Exception {
        AppTemplate actual = appFrameworkService.getAppTemplate("registrationapp.registerPatient");
        assertNotNull(actual);
        assertThat(actual.getId(), is("registrationapp.registerPatient"));
    }

    @Test
    public void testInheritingConfigurationFromAppTemplate() {
        AppTemplate template = appFrameworkService.getAppTemplate("registrationapp.registerPatient");
        AppDescriptor instance = appFrameworkService.getApp("referenceapplication.registerPatient.outpatient");
        assertThat(instance.getTemplate(), is(template));
        assertThat(instance.getConfig().get("extraFields").size(), is(1));
        assertThat(instance.getConfig().get("extraFields").get(0).getTextValue(), is("phoneNumber"));
        assertThat(instance.getConfig().get("urlOnSuccess").getTextValue(), is("patientDashboard.page?patientId={{appContext.createdPatientId}}"));
    }

}
