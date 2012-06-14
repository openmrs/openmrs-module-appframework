package org.openmrs.module.appframework;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class AppFrameworkActivatorTest extends BaseModuleContextSensitiveTest {
	
	AppFrameworkService service;
	
	@Before
	public void before() {
		service = Context.getService(AppFrameworkService.class);
	}
	
	/**
	 * @see AppFrameworkActivator#contextRefreshed()
	 * @verifies set all available apps on {@link AppFrameworkService}
	 */
	@Test
	public void contextRefreshed_shouldSetAllAvailableAppsOnAppFrameworkService() throws Exception {
		new AppFrameworkActivator().contextRefreshed();
		
		Assert.assertEquals(3, service.getAllApps().size());
	}

	/**
     * @see AppFrameworkActivator#contextRefreshed()
     * @verifies create privileges for all available apps
     */
    @Test
    public void contextRefreshed_shouldCreatePrivilegesForAllAvailableApps() throws Exception {
    	new AppFrameworkActivator().contextRefreshed();
    	
    	for (AppDescriptor app : service.getAllApps()) {
    		if (app.getRequiredPrivilegeName() != null) {
    			Assert.assertNotNull(Context.getUserService().getPrivilege(app.getRequiredPrivilegeName()));
    		}
    	}
    }
}