package org.openmrs.module.appframework.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppFrameworkServiceImplTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private AppFrameworkService appFrameworkService;

    @Autowired
    private AllAppDescriptors allAppDescriptors;

    @Autowired
    private AllExtensions allExtensions;

    @Autowired
    private FeatureToggleProperties featureToggles;

    @Before
    public void setUp() throws Exception {

        featureToggles.setPropertiesFile(new File(this.getClass().getResource("/" + FeatureToggleProperties.FEATURE_TOGGLE_PROPERTIES_FILE_NAME).getFile()));

        List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();
        appDescriptors.add(
                new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 20,
                        null, Arrays.asList(new ExtensionPoint("extensionPoint1"), new ExtensionPoint("extensionPoint2"))));
        appDescriptors.add(
                new AppDescriptor("app2", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 10,
                        null, Arrays.asList(new ExtensionPoint("extensionPoint1"))));
        allAppDescriptors.add(appDescriptors);

        // add some extension points to these apps
        allAppDescriptors.getAppDescriptor("app1").setExtensions(Arrays.asList(new Extension("ext1", "app1", "extensionPoint2", "link", "label", "url", 4),
                new Extension("ext2", "app1", "extensionPoint2", "link", "label", "url", 3)));

        allAppDescriptors.getAppDescriptor("app2").setExtensions(Arrays.asList(new Extension("ext3", "app2", "extensionPoint1", "link", "label", "url", 2)));

        // now add some free-standing extension
        allExtensions.add(new Extension("ext4", "", "extensionPoint2", "link", "label", "url", 1));
        allExtensions.add(new Extension("ext5", "", "extensionPoint2", "link", "label", "url", 0));

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
    public void testGetAllEnabledAppsShouldIgnoreAppsToggledOffInFeatureTogglesFile() throws Exception {
        List<AppDescriptor> allApps = appFrameworkService.getAllEnabledApps();

        assertEquals(1, allApps.size());
        assertEquals("app1", allApps.get(0).getId());
    }

    @Test
    public void testGetAllExtensionsAndIsSortedByOrder() throws Exception {
        List<Extension> extensionPoints = appFrameworkService.getAllExtensions("extensionPoint2");

        assertEquals(2, extensionPoints.size());
        assertEquals("ext5", extensionPoints.get(0).getId());
        assertEquals("ext4", extensionPoints.get(1).getId());

    }

    @Test
    public void testGetAllEnabledExtensionsShouldIgnoreEnabledToggledOffInFeatureTogglesFile() throws Exception {
        List<Extension> extensionPoints = appFrameworkService.getAllEnabledExtensions("extensionPoint2");

        assertEquals(2, extensionPoints.size());
        assertEquals("ext5", extensionPoints.get(0).getId());
        assertEquals("ext2", extensionPoints.get(1).getId());
    }
}
