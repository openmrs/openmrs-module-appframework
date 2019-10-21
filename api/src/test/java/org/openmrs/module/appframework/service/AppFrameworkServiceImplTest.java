package org.openmrs.module.appframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.context.AppContextModel;
import org.openmrs.module.appframework.context.ProgramConfiguration;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllFreeStandingExtensions;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class AppFrameworkServiceImplTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private AppFrameworkService appFrameworkService;

    @Autowired
    private AllAppDescriptors allAppDescriptors;

    @Autowired
    private AllFreeStandingExtensions allFreeStandingExtensions;

    @Autowired
    private FeatureToggleProperties featureToggles;
    
    private AppFrameworkServiceImpl appFrameworkServiceImpl;

    private AppDescriptor app1;

    private AppDescriptor app2;

    private Extension ext1;

    private Extension ext2;

    private Extension ext3;

    private Extension ext4;

    private Extension ext5;
    
    private static final String PATIENT_UUID = "0cbe2ed3-cd5f-4f46-9459-26127c9265ab";

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
        
        appFrameworkServiceImpl = new AppFrameworkServiceImpl(null, null, null, null, null, null, null, null);
        
        executeDataSet("AppFrameworkServiceImplTest-createPatientProgram.xml");
    }

    @After
    public void tearDown() throws Exception {
        allAppDescriptors.clear();
        allFreeStandingExtensions.clear();
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
    public void testGetAllEnabledAppsShouldCorrectlyHandleNegatedFeatureToggles() throws Exception {

        // we change these so that app1 should only be enabled if app toggle 1 is not enabled, same for app2
        app1.setFeatureToggle("!app1Toggle");
        app2.setFeatureToggle("!app2Toggle");

        List<AppDescriptor> allApps = appFrameworkService.getAllEnabledApps();

        assertEquals(1, allApps.size());
        assertEquals("app2", allApps.get(0).getId());
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

    @Test
    public void testGetAllEnabledExtensionsShouldCorrectlyHandleNegatedFeatureToggles() throws Exception {

        // invert the feature toggles
        ext1.setFeatureToggle("!ext1Toggle");
        ext2.setFeatureToggle("!ext2Toggle");
        ext3.setFeatureToggle("!ext3Toggle");
        ext4.setFeatureToggle("!ext4Toggle");
        ext5.setFeatureToggle("!ext5Toggle");

        List<Extension> extensionPoints = appFrameworkService.getAllEnabledExtensions("extensionPoint2");

        assertEquals(2, extensionPoints.size());
        assertEquals("ext4", extensionPoints.get(0).getId());
        assertEquals("ext1", extensionPoints.get(1).getId());

    }

    @Test
    public void testCheckRequireExpression() throws Exception {
        VisitStatus visit = new VisitStatus(true, false);
        AppContextModel contextModel = new AppContextModel();
        contextModel.put("visit", visit);

        assertTrue(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("visit.active"), contextModel));
        assertTrue(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("visit.active || visit.admitted"), contextModel));
        assertFalse(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("visit.admitted"), contextModel));
        assertFalse(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("visit.admitted && visit.admitted"), contextModel));
    }

    @Test
    public void testCheckRequireExpressionWithMapProperty() throws Exception {
        AppContextModel contextModel = new AppContextModel();
        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("uuid", "abc-123");
        contextModel.put("sessionLocation", obj);

        assertTrue(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("sessionLocation.uuid == 'abc-123'"), contextModel));
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

        assertTrue(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("hasMemberWithProperty(sessionLocation.tags, 'display', 'Login Location')"), contextModel));
        assertFalse(appFrameworkServiceImpl.checkRequireExpression(extensionRequiring("hasMemberWithProperty(sessionLocation.tags, 'display', 'Not this tag')"), contextModel));
    }

    @Test
    public void testGetPatientUuid() throws ScriptException {
    	// Setup
    	AppContextModel contextModel = new AppContextModel();
    	contextModel.put("patient", new PatientContextModel(PATIENT_UUID));
    	
    	// Replay
        String uuid = appFrameworkServiceImpl.getPatientUuid(contextModel);
        
        // Verify
        assertEquals(PATIENT_UUID, uuid);
    }
    
    @Test
    public void testCheckRequiredProgramsOnCurrentPatient() {
    	// Setup
    	final String STATE_NOT_IN_CURRENT_PATIENT_STATES = "588a31bb-7923-4ef8-a6fc-b8f2ae5d1352";
    	AppContextModel contextModel = new AppContextModel();
    	contextModel.put("patient", new PatientContextModel(PATIENT_UUID));
    	
    	assertTrue(appFrameworkServiceImpl.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(new ProgramConfiguration("CIEL:123", "1743", "CIEL:124"))), 
    			contextModel));
    	assertTrue(appFrameworkServiceImpl.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(new ProgramConfiguration(null, "1743", "CIEL:124"))), 
    			contextModel));
    	// Configuration with a state that's not a member of the patient's current states
    	assertFalse(appFrameworkServiceImpl.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(new ProgramConfiguration(null, null, STATE_NOT_IN_CURRENT_PATIENT_STATES))), 
    			contextModel));
    	assertTrue(appFrameworkServiceImpl.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(new ProgramConfiguration(null, null, STATE_NOT_IN_CURRENT_PATIENT_STATES),
    			new ProgramConfiguration("CIEL:123", "1743", null))), contextModel));
    }
    
    @Test
    public void hasProgramAssignableToConfiguration_shouldReturnTrueIfConfigIsAssignableToAnyOfThePatientPrograms() throws ScriptException {
    	// Setup
    	List<PatientProgram> patientPrograms = Context.getProgramWorkflowService().getPatientPrograms(new Patient(2), 
    			null, null, null, null, null, false);
    	ProgramConfiguration config = new ProgramConfiguration("CIEL:123", "1743", "CIEL:124");

    	// Replay
        boolean isAssignable = appFrameworkServiceImpl.hasProgramAssignableToConfiguration(patientPrograms, config);
        
        // Verify
        assertTrue(isAssignable);
    }
    
    @Test
    public void hasProgramAssignableToConfiguration_shouldReturnFalseIfConfigIsNotAssignableToAnyOfThePatientPrograms() throws ScriptException {
    	// Setup
    	List<PatientProgram> patientPrograms = Context.getProgramWorkflowService().getPatientPrograms(new Patient(2), 
    			null, null, null, null, null, false);
    	// Configuration with a state that's not a member of the patient's current states
    	ProgramConfiguration config = new ProgramConfiguration("CIEL:123", "1743", "1740");

    	// Replay
        boolean isAssignable = appFrameworkServiceImpl.hasProgramAssignableToConfiguration(patientPrograms, config);
        
        // Verify
        assertFalse(isAssignable);
    }
    
    @Test
    public void hasProgramAssignableToConfiguration_shouldFailWithAnExceptionIfConfigurationHasAnInvalidProgramTree() {
    	// Setup
    	List<PatientProgram> patientPrograms = Context.getProgramWorkflowService().getPatientPrograms(new Patient(2), 
    			null, null, null, null, null, false);
    	// Configuration with invalid program tree
    	ProgramConfiguration config = new ProgramConfiguration("CIEL:124", "1738", "1743");
    	
    	try {
    		// Replay
    		appFrameworkServiceImpl.hasProgramAssignableToConfiguration(patientPrograms, config);
    		fail("Should throw exception if configuration has an invalid program tree");
    	} catch(APIException e) {
    		// Verify
    		assertEquals("ProgramConfiguration has an invalid program tree", e.getMessage());
    	}
    }
    
    private Extension extensionRequiring(String requires) {
        Extension extension = new Extension();
        extension.setRequire(requires);
        return extension;
    }
    
    private Extension extensionRequiringProgramConfigurations(List<ProgramConfiguration> programConfigurations) {
    	Extension extension = new Extension();
        extension.setRequiredPrograms(programConfigurations);
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
    
    public static class PatientContextModel {
    	public String uuid;
    	public Patient patient;
    	public PatientContextModel(String uuid) {
    		this.uuid = uuid;
    	}
    }
}
