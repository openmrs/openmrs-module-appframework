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

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.RoleConstants;

/**
 * Tests {@link ${AppFrameworkService}}.
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
	
}
