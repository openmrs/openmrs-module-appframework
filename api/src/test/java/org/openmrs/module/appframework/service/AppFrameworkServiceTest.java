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

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.TestLoginLocationFilter;
import org.openmrs.module.appframework.config.AppFrameworkConfig;
import org.openmrs.module.appframework.context.AppContextModel;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.openmrs.util.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class AppFrameworkServiceTest extends BaseModuleContextSensitiveTest {
	
	public static final String LOCATION_UUID1 = "6f42abbc-caac-40ae-a94e-9277ea15c125";
	
	public static final String LOCATION_UUID2 = "400d343b-75ed-4243-af42-7b9b1b72f0a9";
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private AppFrameworkService appFrameworkService;
	
	@Autowired
	private AppFrameworkConfig appFrameworkConfig;
	
	@Before
	public void setup() throws IOException {
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
		
		return us.createUser(u, "Openmr5xy");
	}
	
	/**
	 * @see {@link AppFrameworkService#getAllEnabledExtensions()}
	 */
	@Test
	public void getAllEnabledExtensions_shouldGetAllEnabledExtensions() throws Exception {
		List<Extension> visitExts = appFrameworkService.getAllEnabledExtensions();
		assertEquals(4, visitExts.size());
		assertThat(visitExts,
		    containsInAnyOrder(hasProperty("id", is("registerOutpatientHomepageLink")),
		        hasProperty("id", is("orderXrayExtension")), hasProperty("id", is("gotoPatientExtension")),
		        hasProperty("id", is("gotoArchives"))));
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
	public void getExtensionsForCurrentUser_shouldReturnExtensionsWithNoRequiredPrivilegeIfThereIsNoAuthenticatedUser()
	    throws Exception {
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
	public void getExtensionsForCurrentUser_shouldReturnNoExtensionForNoLoggedInUser() throws Exception {
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
	public void getExtensionsForCurrentUser_shouldReturnNoExtensionForUserWithoutPrivilege() throws Exception {
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
		us.createUser(user, "Openmr5xy");
		
		Context.authenticate(user.getUsername(), "Openmr5xy");
		assertEquals(user, Context.getAuthenticatedUser());
		
		List<Extension> userExts = appFrameworkService.getExtensionsForCurrentUser("activeVisitActions");
		assertEquals(1, userExts.size());
		assertEquals("orderXrayExtension", userExts.get(0).getId());
	}
	
	/**
	 * @see AppFrameworkService#getExtensionsForCurrentUser(String, Bindings)
	 * @verifies get enabled extensions for the current user whose require property matches the
	 *           contextModel
	 */
	@Test
	public void getExtensionsForCurrentUser_shouldGetEnabledExtensionsForTheCurrentUserByRequireProperty() throws Exception {
		User user = setupPrivilegesRolesAndUser("Some Random Privilege");
		Context.authenticate(user.getUsername(), "Openmr5xy");
		assertEquals(user, Context.getAuthenticatedUser());
		
		AppContextModel contextModel = setupContextModel(true);
		
		List<Extension> extensions = appFrameworkService.getExtensionsForCurrentUser(null, contextModel);
		assertEquals(1, extensions.size());
		assertEquals("orderXrayExtension", extensions.get(0).getId());
		
		contextModel = setupContextModel(false);
		extensions = appFrameworkService.getExtensionsForCurrentUser(null, contextModel);
		assertEquals(1, extensions.size());
		assertEquals("gotoArchives", extensions.get(0).getId());
	}
	
	private AppContextModel setupContextModel(boolean isVisitActive) {
		AppContextModel bindings = new AppContextModel();
		bindings.put("patientId", 7);
		bindings.put("visit", new VisitStatus(isVisitActive));
		return bindings;
	}
	
	@Test
	public void getAllAppTemplates_shouldGetAppTemplates() throws Exception {
		List<AppTemplate> actual = appFrameworkService.getAllAppTemplates();
		assertThat(actual.size(), is(2));
		assertThat(actual.get(0).getId(), is("testing.registrationapp.registerPatient"));
	}
	
	@Test
	public void getAppTemplateById_shouldGetAppTemplate() throws Exception {
		AppTemplate actual = appFrameworkService.getAppTemplate("testing.registrationapp.registerPatient");
		assertNotNull(actual);
		assertThat(actual.getId(), is("testing.registrationapp.registerPatient"));
	}
	
	@Test
	public void testInheritingConfigurationFromAppTemplate() {
		AppTemplate template = appFrameworkService.getAppTemplate("testing.registrationapp.registerPatient");
		AppDescriptor instance = appFrameworkService.getApp("referenceapplication.registerPatient.outpatient");
		assertThat(instance.getTemplate(), is(template));
		assertThat(instance.getConfig().size(), is(3));
		assertThat(instance.getConfig().get("extraFields").size(), is(1));
		assertThat(instance.getConfig().get("extraFields").get(0).getTextValue(), is("phoneNumber"));
		assertThat(instance.getConfig().get("urlOnSuccess").getTextValue(),
		    is("patientDashboard.page?patientId={{appContext.createdPatientId}}"));
		assertThat(instance.getConfig().get("configDefinedOnlyInAppDescription").getTextValue(), is("someValue"));
	}
	
	@Test
	@Verifies(value = "should get all enabled apps", method = "getAllEnabledApps()")
	public void getAllEnabledApps_shouldGetAllEnabledApps() throws Exception {
		List<AppDescriptor> apps = appFrameworkService.getAllEnabledApps();
		assertEquals(4, apps.size());//should include the app with that requires no privilege
		List<String> appIds = new ArrayList<String>();
		for (AppDescriptor app : apps) {
			appIds.add(app.getId());
		}
		assertTrue(appIds.contains("patientDashboardApp"));
		assertTrue(appIds.contains("archiveRoomApp"));
		assertTrue(appIds.contains("xrayApp"));
		assertTrue(appIds.contains("referenceapplication.registerPatient.outpatient"));
	}
	
	/**
	 * @verifies return a user app that matches the specified appId
	 * @see AppFrameworkService#getUserApp(String)
	 */
	@Test
	public void getUserApp_shouldReturnAUserAppThatMatchesTheSpecifiedAppId() throws Exception {
		executeDataSet("moduleTestData.xml");
		String expectedJson = "{\"id\":\"test.someApp\",\"description\":\"Some User App\"}";
		UserApp app = appFrameworkService.getUserApp("test.someApp");
		assertNotNull(app);
		assertEquals(expectedJson, app.getJson());
	}
	
	/**
	 * @verifies return a list of UserApps
	 * @see AppFrameworkService#getUserApps()
	 */
	@Test
	public void getUserApps_shouldReturnAListOfUserApps() throws Exception {
		executeDataSet("moduleTestData.xml");
		assertEquals(2, appFrameworkService.getUserApps().size());
	}
	
	/**
	 * @verifies save the user app to the database and update the list of loaded apps
	 * @see AppFrameworkService#saveUserApp(org.openmrs.module.appframework.domain.UserApp)
	 */
	@Test
	public void saveUserApp_shouldSaveTheUserAppToTheDatabaseAndUpdateTheListOfLoadedApps() throws Exception {
		String appId = "test.myApp";
		assertNull(appFrameworkService.getUserApp(appId));
		int originalAppDescriptorCount = appFrameworkService.getAllApps().size();
		UserApp userApp = new UserApp();
		userApp.setAppId(appId);
		userApp.setJson("{\"id\":\"" + appId + "\",\"description\":\"My App Description\"}");
		appFrameworkService.saveUserApp(userApp);
		assertNotNull(appFrameworkService.getUserApp(appId));
		assertEquals(++originalAppDescriptorCount, appFrameworkService.getAllApps().size());
	}
	
	/**
	 * @verifies return a user app that matches the specified id
	 * @see AppFrameworkService#getApp(String)
	 */
	@Test
	public void getApp_shouldReturnAUserAppThatMatchesTheSpecifiedId() throws Exception {
		executeDataSet("moduleTestData.xml");
		//Reload the apps to pick up the ones in our test dataset
		new AppFrameworkActivator().contextRefreshed();
		String expectedDescription = "Some User App";
		AppDescriptor app = appFrameworkService.getApp("test.someApp");
		assertNotNull(app);
		assertEquals(expectedDescription, app.getDescription());
	}
	
	/**
	 * @verifies remove the user app from the database and update the list of loaded apps
	 * @see AppFrameworkService#purgeUserApp(org.openmrs.module.appframework.domain.UserApp)
	 */
	@Test
	public void purgeUserApp_shouldRemoveTheUserAppFromTheDatabaseAndUpdateTheListOfLoadedApps() throws Exception {
		executeDataSet("moduleTestData.xml");
		//Reload the apps to pick up the ones in our test dataset
		new AppFrameworkActivator().contextRefreshed();
		UserApp app = appFrameworkService.getUserApp("test.someApp");
		assertNotNull(app);
		int originalAppDescriptorCount = appFrameworkService.getAllApps().size();
		appFrameworkService.purgeUserApp(app);
		app = appFrameworkService.getUserApp("test.someApp");
		assertNull(app);
		assertEquals(--originalAppDescriptorCount, appFrameworkService.getAllApps().size());
	}
	
	@DirtiesContext
	@Test
	public void getLoginLocations_shouldReturnLoginLocationsConfiguredForTheUser() {
		LocationTag tag = new LocationTag();
		tag.setName(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN);
		locationService.saveLocationTag(tag);
		createLocation("some uuid a");
		createLocation("some uuid b");
		Location location1 = createLocation(LOCATION_UUID1);
		Location location2 = createLocation(LOCATION_UUID2);
		List<Location> locations = appFrameworkService.getLoginLocations();
		assertEquals(4, locations.size());
		Context.getRegisteredComponents(TestLoginLocationFilter.class).get(0).enable();
		locations = appFrameworkService.getLoginLocations();
		assertEquals(2, locations.size());
		assertTrue(locations.contains(location1));
		assertTrue(locations.contains(location2));
	}
	
	private Location createLocation(String uuid) {
		Location location = new Location();
		location.setUuid(uuid);
		location.setName("Some name " + uuid);
		location.addTag(locationService.getLocationTagByName(AppFrameworkConstants.LOCATION_TAG_SUPPORTS_LOGIN));
		return locationService.saveLocation(location);
	}
	
	public class VisitStatus {
		
		public int id = 17;
		
		public boolean active;
		
		public VisitStatus(boolean visitActive) {
			this.active = visitActive;
		}
	}
}
