package org.openmrs.module.appframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validator;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.appframework.config.AppFrameworkConfig;
import org.openmrs.module.appframework.context.AppContextModel;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appframework.feature.TestFeatureTogglePropertiesFactory;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllComponentsState;
import org.openmrs.module.appframework.repository.AllFreeStandingExtensions;

public class AppFrameworkServiceImplTest  {
	
	private Validator validator = mock(Validator.class);
	
    private AllAppDescriptors allAppDescriptors = new AllAppDescriptors(validator);

    private AllFreeStandingExtensions allFreeStandingExtensions = new AllFreeStandingExtensions(validator);
    
    private AllComponentsState allComponentsState = new AllComponentsState();

    private FeatureToggleProperties featureToggles = TestFeatureTogglePropertiesFactory.get();

    private AppFrameworkConfig appFrameworkConfig = new AppFrameworkConfig();

    private AppDescriptor app1;

    private AppDescriptor app2;

    private Extension ext1;

    private Extension ext2;

    private Extension ext3;

    private Extension ext4;

    private Extension ext5;
    
    private AppFrameworkServiceImpl service;

    @Before
    public void setUp() throws Exception {

        featureToggles.setPropertiesFile(new File(this.getClass().getResource("/" + FeatureToggleProperties.FEATURE_TOGGLE_PROPERTIES_FILE_NAME).getFile()));

        app1 = new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 20,
                null, Arrays.asList(new ExtensionPoint("extensionPoint1"), new ExtensionPoint("extensionPoint2")));

        app2 = new AppDescriptor("app2", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 10,
                null, Arrays.asList(new ExtensionPoint("extensionPoint1")));

        ext1 = new Extension("ext1", "app1", "extensionPoint2", "link", "label", "url", 4);
        ext2 = new Extension("ext2", "app1", "extensionPoint2", "link", "label", "url", 3);
        ext3 = new Extension("ext3", "app2", "extensionPoint1", "link", "label", "url", 2);
        ext4 = new Extension("ext4", "", "extensionPoint2", "link", "label", "url", 1);
        ext5 = new Extension("ext5", "", "extensionPoint2", "link", "label", "url", 0);

        // add some feature toggles to these apps & extensions
        app1.setFeatureToggle("app1Toggle");
        app2.setFeatureToggle("app2Toggle");
        ext1.setFeatureToggle("ext1Toggle");
        ext2.setFeatureToggle("ext2Toggle");
        ext3.setFeatureToggle("ext3Toggle");
        ext4.setFeatureToggle("ext4Toggle");
        ext5.setFeatureToggle("ext5Toggle");

        // add the some of the extensions to the apps
        app1.setExtensions(Arrays.asList(ext1, ext2));
        app2.setExtensions(Arrays.asList(ext3));

        allAppDescriptors.add(Arrays.asList(app1, app2));

        // now add some free-standing extension
        allFreeStandingExtensions.add(Arrays.asList(ext3, ext4, ext5));
        
        // to go through all the Hibernate stuff
        SessionFactory sessionFactory = mock(SessionFactory.class);
    	Session session = mock(Session.class);
    	when(session.createCriteria(any(Class.class))).thenReturn(mock(Criteria.class));
    	when(sessionFactory.getCurrentSession()).thenReturn(session);
    	allComponentsState.setSessionFactory(new DbSessionFactory(sessionFactory));
    	
    	service = new AppFrameworkServiceImpl(null, allAppDescriptors, allFreeStandingExtensions, allComponentsState, null, featureToggles, appFrameworkConfig, null);
    }

    @After
    public void tearDown() throws Exception {
        allAppDescriptors.clear();
        allFreeStandingExtensions.clear();
    }

    @Test
    public void testGetAllAppsAndIsSortedByOrder() throws Exception {
        List<AppDescriptor> allApps = service.getAllApps();

        assertEquals(2, allApps.size());
        assertEquals("app2", allApps.get(0).getId());
        assertEquals("app1", allApps.get(1).getId());
    }

    @Test
    public void testGetAllEnabledAppsShouldIgnoreAppsToggledOffInFeatureTogglesFile() throws Exception {
        List<AppDescriptor> allApps = service.getAllEnabledApps();

        assertEquals(1, allApps.size());
        assertEquals("app1", allApps.get(0).getId());
    }

    @Test
    public void testGetAllEnabledAppsShouldCorrectlyHandleNegatedFeatureToggles() throws Exception {
        // we change these so that app1 should only be enabled if app toggle 1 is not enabled, same for app2
        app1.setFeatureToggle("!app1Toggle");
        app2.setFeatureToggle("!app2Toggle");

        List<AppDescriptor> allApps = service.getAllEnabledApps();

        assertEquals(1, allApps.size());
        assertEquals("app2", allApps.get(0).getId());
    }

    @Test
    public void testGetAllExtensionsAndIsSortedByOrder() throws Exception {
        List<Extension> extensionPoints = service.getAllExtensions("extensionPoint2");

        assertEquals(2, extensionPoints.size());
        assertEquals("ext5", extensionPoints.get(0).getId());
        assertEquals("ext4", extensionPoints.get(1).getId());

    }

    @Test
    public void testGetAllEnabledExtensionsShouldIgnoreEnabledToggledOffInFeatureTogglesFile() throws Exception {
        List<Extension> extensionPoints = service.getAllEnabledExtensions("extensionPoint2");

        assertEquals(2, extensionPoints.size());
        assertEquals("ext5", extensionPoints.get(0).getId());
        assertEquals("ext2", extensionPoints.get(1).getId());
    }

    @Test
    public void testGetAllEnabledExtensionsShouldCorrectlyHandleNegatedFeatureToggles() throws Exception {
        // invert the feature toggles
        ext1.setFeatureToggle("!ext1Toggle");
        ext2.setFeatureToggle("!ext2Toggle");
        ext3.setFeatureToggle("!ext3Toggle");
        ext4.setFeatureToggle("!ext4Toggle");
        ext5.setFeatureToggle("!ext5Toggle");
        
        List<Extension> extensionPoints = service.getAllEnabledExtensions("extensionPoint2");

        assertEquals(2, extensionPoints.size());
        assertEquals("ext4", extensionPoints.get(0).getId());
        assertEquals("ext1", extensionPoints.get(1).getId());
    }

    @Test
    public void testCheckRequireExpression() throws Exception {
        VisitStatus visit = new VisitStatus(true, false);
        AppContextModel contextModel = new AppContextModel();
        contextModel.put("visit", visit);

        assertTrue(service.checkRequireExpression(extensionRequiring("visit.active"), contextModel));
        assertTrue(service.checkRequireExpression(extensionRequiring("visit.active || visit.admitted"), contextModel));
        assertFalse(service.checkRequireExpression(extensionRequiring("visit.admitted"), contextModel));
        assertFalse(service.checkRequireExpression(extensionRequiring("visit.admitted && visit.admitted"), contextModel));
    }

    @Test
    public void testCheckRequireExpressionWithMapProperty() throws Exception {
        AppContextModel contextModel = new AppContextModel();
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("uuid", "abc-123");
        contextModel.put("sessionLocation", obj);

        assertTrue(service.checkRequireExpression(extensionRequiring("sessionLocation.uuid == 'abc-123'"), contextModel));
    }

    @Test
    public void testUtilityFunctionForRequireExpressions() throws Exception {
        AppContextModel contextModel = new AppContextModel();
        Map<String, Object> tag = new HashMap<String, Object>();
        tag.put("display", "Login Location");
        List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
        tags.add(tag);
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("tags", tags);
        contextModel.put("sessionLocation", obj);

        assertTrue(service.checkRequireExpression(extensionRequiring("hasMemberWithProperty(sessionLocation.tags, 'display', 'Login Location')"), contextModel));
        assertFalse(service.checkRequireExpression(extensionRequiring("hasMemberWithProperty(sessionLocation.tags, 'display', 'Not this tag')"), contextModel));
    }

    private Extension extensionRequiring(String requires) {
        Extension extension = new Extension();
        extension.setRequire(requires);
        return extension;
    }

    public class VisitStatus {
        public boolean active;
        public boolean admitted;
        public VisitStatus(boolean active, boolean admitted) {
            this.active = active;
            this.admitted = admitted;
        }
    }
}
