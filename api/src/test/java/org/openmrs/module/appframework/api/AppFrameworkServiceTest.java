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
package org.openmrs.module.appframework.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.module.appframework.AppFrameworkConstants;
import org.openmrs.module.appframework.SimpleAppDescriptor;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.RoleConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link AppFrameworkService}.
 */
public class  AppFrameworkServiceTest extends BaseModuleContextSensitiveTest {
	
	AppFrameworkService service;
	
	@Before
	public void beforeEachTest() {
		service = Context.getService(AppFrameworkService.class);
		new AppFrameworkActivator().contextRefreshed();
	}
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(AppFrameworkService.class));
	}

	/**
     * @see AppFrameworkService#getAppsForUser(User)
     * @verifies get apps that a particular user has privileges for
     */
    @Test
    public void getAppsForUser_shouldGetAppsThatAParticularUserHasPrivilegesFor() throws Exception {
	    // setup test data
    	UserService userService = Context.getUserService();
    	User user = userService.getUser(502);
    	Assert.assertNotNull(user);
    	Assert.assertFalse(user.hasRole(RoleConstants.SUPERUSER));
    	Assert.assertTrue(user.hasRole(RoleConstants.PROVIDER));
    	
    	Role provider = userService.getRole(RoleConstants.PROVIDER);
    	Assert.assertNotNull(provider);

    	// first check no apps are enabled
    	List<AppDescriptor> enabled = service.getAppsForUser(user);
    	Assert.assertEquals(0, enabled.size());

    	// now enable one
    	AppDescriptor enableThis = service.getAllApps().get(0);
    	Privilege priv = service.ensurePrivilegeExists(enableThis);
    	provider.addPrivilege(priv);
    	userService.saveRole(provider);
    	
    	// check that it's enabled
    	enabled = service.getAppsForUser(user);
    	Assert.assertEquals(1, enabled.size());
    	Assert.assertEquals(enableThis, enabled.get(0));
    }
    
    
    /**
     * @see AppFrameworkService#getAppsForUser(User)
     * @verifies get apps that a particular user has privileges for
     */
    @Test
    public void getAppsForUser_shouldSortAppsAccordingToUserProperty() throws Exception {
	    // setup test data
    	UserService userService = Context.getUserService();
    	User user = userService.getUser(502);
    	Assert.assertNotNull(user);
    	Assert.assertFalse(user.hasRole(RoleConstants.SUPERUSER));
    	Assert.assertTrue(user.hasRole(RoleConstants.PROVIDER));
    	
    	Role provider = userService.getRole(RoleConstants.PROVIDER);
    	Assert.assertNotNull(provider);

    	// first check no apps are enabled
    	List<AppDescriptor> enabled = service.getAppsForUser(user);
    	Assert.assertEquals(0, enabled.size());

    	// now enable one
    	AppDescriptor enableThis = service.getAllApps().get(0);
    	Privilege priv = service.ensurePrivilegeExists(enableThis);
    	provider.addPrivilege(priv);
    	// and another
    	enableThis = service.getAllApps().get(1);
    	priv = service.ensurePrivilegeExists(enableThis);
    	provider.addPrivilege(priv);
    	userService.saveRole(provider);
    	
    	// reverse the order in the sorting
    	List<AppDescriptor> apps = service.getAppsForUser(user);
    	String sortOrder = apps.get(1).getId() + "," + apps.get(0).getId();
    	user.setUserProperty(AppFrameworkConstants.APP_SORT_ORDER_USER_PROPERTY, sortOrder);
    	userService.saveUser(user, null);
    	
    	List<AppDescriptor> appsAfterSorting = service.getAppsForUser(user);
    	Assert.assertEquals(apps.get(0).getId(), appsAfterSorting.get(1).getId());
    	Assert.assertEquals(apps.get(1).getId(), appsAfterSorting.get(0).getId());
    }

    @Test
    public void getAllAppsOrdered() throws Exception {
        SimpleAppDescriptor firstApp = new SimpleAppDescriptor("4", "App1", 2);
        SimpleAppDescriptor secondApp = new SimpleAppDescriptor("2", "App2", 8);
        SimpleAppDescriptor thirdApp = new SimpleAppDescriptor("1", "App3", 10);
        SimpleAppDescriptor fourthApp = new SimpleAppDescriptor("3", "App4", 13);
        SimpleAppDescriptor fifthApp = new SimpleAppDescriptor("5", "App5");

        List<AppDescriptor> apps = new ArrayList<AppDescriptor>();
        apps.addAll(Arrays.asList(thirdApp, fifthApp, secondApp, fourthApp, firstApp));
        service.setAllApps(apps);

        List<AppDescriptor> orderedApps = service.getAllApps();

        List<AppDescriptor> expectedApps = new ArrayList<AppDescriptor>();
        expectedApps.addAll(Arrays.asList(firstApp, secondApp, thirdApp, fourthApp, fifthApp));
        assertThat(orderedApps, is(expectedApps));
    }

    @Test
    public void getAppsOrderedForAnUser() throws Exception {
        User user = Mockito.mock(User.class);
        Mockito.when(user.hasPrivilege(Mockito.argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                String privilegedName = (String) argument;
                return !privilegedName.matches("App: 2");
            }
        }))).thenReturn(true);

        SimpleAppDescriptor firstApp = new SimpleAppDescriptor("4", "App1", 2);
        SimpleAppDescriptor forbiddenApp = new SimpleAppDescriptor("2", "App2", 8);
        SimpleAppDescriptor secondApp = new SimpleAppDescriptor("1", "App3", 10);
        SimpleAppDescriptor thirdApp = new SimpleAppDescriptor("3", "App4", 13);
        SimpleAppDescriptor fourthApp = new SimpleAppDescriptor("5", "App5");

        List<AppDescriptor> apps = new ArrayList<AppDescriptor>();
        apps.addAll(Arrays.asList(thirdApp, forbiddenApp, secondApp, fourthApp, firstApp));
        service.setAllApps(apps);

        List<AppDescriptor> orderedApps = service.getAppsForUser(user);

        List<AppDescriptor> expectedApps = new ArrayList<AppDescriptor>();
        expectedApps.addAll(Arrays.asList(firstApp, secondApp, thirdApp, fourthApp));
        assertThat(orderedApps, is(expectedApps));
    }
	
}
