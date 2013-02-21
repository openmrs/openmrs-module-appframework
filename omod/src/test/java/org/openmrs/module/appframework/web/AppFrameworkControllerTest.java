package org.openmrs.module.appframework.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.appframework.SimpleAppDescriptor;
import org.openmrs.module.appframework.api.AppFrameworkService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest(Context.class)
@RunWith(PowerMockRunner.class)
public class AppFrameworkControllerTest {

    @Mock
    AppFrameworkService mockAppFrameworkService;

    private List<AppDescriptor> getTestAppList() {
        List<AppDescriptor> appDescriptorList = new ArrayList<AppDescriptor>();
        appDescriptorList.add(new SimpleAppDescriptor("id1", "label1", 0));
        appDescriptorList.add(new SimpleAppDescriptor("id2", "label2", 1));
        appDescriptorList.add(new SimpleAppDescriptor("id2", "label3", 2));
        return appDescriptorList;
    }

    @Test
    public void testGetAllAppsWhenUserNotAuthenticated() throws Exception {
        mockStatic(Context.class);
        when(Context.getService(AppFrameworkService.class)).thenReturn(mockAppFrameworkService);
        when(Context.getAuthenticatedUser()).thenReturn(null);
        when(mockAppFrameworkService.getAllApps()).thenReturn(getTestAppList());

        List<AppDescriptor> appList = new AppFrameworkController().getAppList();

        assertNotNull(appList);
        assertEquals(3, appList.size());
        assertEquals("id1", appList.get(0).getId());
    }

    @Test
    public void testGetAllAppsWhenUserIsAuthenticated() throws Exception {
        mockStatic(Context.class);
        when(Context.getService(AppFrameworkService.class)).thenReturn(mockAppFrameworkService);
        User mockUser = new User();
        when(Context.getAuthenticatedUser()).thenReturn(mockUser);
        when(mockAppFrameworkService.getAppsForUser(mockUser)).thenReturn(getTestAppList());

        List<AppDescriptor> appList = new AppFrameworkController().getAppList();

        assertNotNull(appList);
        assertEquals(3, appList.size());
        assertEquals("id1", appList.get(0).getId());
    }

}
