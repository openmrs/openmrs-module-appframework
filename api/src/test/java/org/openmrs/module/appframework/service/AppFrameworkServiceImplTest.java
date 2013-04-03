package org.openmrs.module.appframework.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.service.AppFrameworkServiceImpl;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AppFrameworkServiceImplTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private AppFrameworkServiceImpl appFrameworkService;

    @Autowired
    private AllAppDescriptors allAppDescriptors;

    @Autowired
    private AllExtensions allExtensions;

    @Before
    public void setUp() throws Exception {
        List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();
        appDescriptors.add(
                new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 20,
                        Arrays.asList(new ExtensionPoint("extensionPoint1"), new ExtensionPoint("extensionPoint2"))));
        appDescriptors.add(
                new AppDescriptor("app2", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 10,
                        Arrays.asList(new ExtensionPoint("extensionPoint1"))));
        allAppDescriptors.add(appDescriptors);

        List<Extension> extensions = new ArrayList<Extension>();
        extensions.add(new Extension("ext1", "app1", "extensionPoint2", "link", "label", "url", 1));
        extensions.add(new Extension("ext2", "app1", "extensionPoint2", "link", "label", "url", 0));
        extensions.add(new Extension("ext3", "app2", "extensionPoint1", "link", "label", "url", 2));
        allExtensions.add(extensions);
    }

    @After
    public void tearDown() throws Exception {
        allAppDescriptors.clear();
        allExtensions.clear();
    }

    @Test
    public void testGetAllAppsAndIsSortedByOrder() throws Exception {
        List<AppDescriptor> allApps = appFrameworkService.getAllApps();

        assertEquals(2, allApps.size());
        assertEquals("app2", allApps.get(0).getId());
        assertEquals("app1", allApps.get(1).getId());
    }

    @Test
    public void testGetAllExtensionsAndIsSortedByOrder() throws Exception {
        List<Extension> extensionsApp1ExtensionPoint2 = appFrameworkService.getAllExtensions("app1", "extensionPoint2");

        assertEquals(2, extensionsApp1ExtensionPoint2.size());
        assertEquals("ext2", extensionsApp1ExtensionPoint2.get(0).getId());
        assertEquals("ext1", extensionsApp1ExtensionPoint2.get(1).getId());

        List<Extension> extensionsApp2ExtensionPoint1 = appFrameworkService.getAllExtensions("app2", "extensionPoint1");

        assertEquals(1, extensionsApp2ExtensionPoint1.size());
        assertEquals("ext3", extensionsApp2ExtensionPoint1.get(0).getId());
    }
}
