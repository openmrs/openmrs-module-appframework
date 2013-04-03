package org.openmrs.module.appframework.web;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appframework.domain.AppDescriptor;
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
        appDescriptorList.add(new AppDescriptor("app1", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 0));
        appDescriptorList.add(new AppDescriptor("app2", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 1));
        appDescriptorList.add(new AppDescriptor("app3", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 2));
        return appDescriptorList;
    }

    @Test
    public void testGetAllApps() throws Exception {
        mockStatic(Context.class);
        when(Context.getService(AppFrameworkService.class)).thenReturn(mockAppFrameworkService);
        when(mockAppFrameworkService.getAllApps()).thenReturn(getTestAppList());

        List<AppDescriptor> appList = new AppFrameworkController().getApps();

        assertNotNull(appList);
        assertEquals(3, appList.size());
        assertEquals("app1", appList.get(0).getId());
    }

}
