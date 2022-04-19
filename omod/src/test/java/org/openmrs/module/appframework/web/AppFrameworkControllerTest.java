package org.openmrs.module.appframework.web;

import org.junit.Test;
import org.mockito.Mockito;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class AppFrameworkControllerTest {

    private List<AppDescriptor> getTestAppList() {
        List<AppDescriptor> appDescriptorList = new ArrayList<AppDescriptor>();
        appDescriptorList.add(new AppDescriptor("app1", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 0));
        appDescriptorList.add(new AppDescriptor("app2", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 1));
        appDescriptorList.add(new AppDescriptor("app3", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 2));
        return appDescriptorList;
    }

    @Test
    public void testGetAllApps() throws Exception {
        AppFrameworkService mockAppFrameworkService = Mockito.mock(AppFrameworkService.class);
        when(mockAppFrameworkService.getAllApps()).thenReturn(getTestAppList());
        AppFrameworkController appFrameworkController = new AppFrameworkController();
        appFrameworkController.setAppFrameworkService(mockAppFrameworkService);
        List<AppDescriptor> appList = appFrameworkController.getApps();
        assertNotNull(appList);
        assertEquals(3, appList.size());
        assertEquals("app1", appList.get(0).getId());
    }

}
