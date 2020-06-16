package org.openmrs.module.appframework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.validation.Validator;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.appframework.LoginLocationFilter;
import org.openmrs.module.appframework.config.AppFrameworkConfig;
import org.openmrs.module.appframework.context.AppContextModel;
import org.openmrs.module.appframework.context.ProgramConfiguration;
import org.openmrs.module.appframework.context.ProgramConfiguration.ResolvedConfiguration;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.appframework.feature.FeatureToggleProperties;
import org.openmrs.module.appframework.feature.TestFeatureTogglePropertiesFactory;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllComponentsState;
import org.openmrs.module.appframework.repository.AllFreeStandingExtensions;
import org.openmrs.module.appframework.repository.AllLoginLocations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class AppFrameworkServiceImplTest  {
	
	@Mock()
	private Validator validator;
	
    private AllAppDescriptors allAppDescriptors;

    private AllFreeStandingExtensions allFreeStandingExtensions;
    
    private AllComponentsState allComponentsState = new AllComponentsState();

    private AllLoginLocations allLoginLocations = new AllLoginLocations();

    private FeatureToggleProperties featureToggles = TestFeatureTogglePropertiesFactory.get();

    private AppFrameworkConfig appFrameworkConfig = new AppFrameworkConfig();
    
    private AppDescriptor app1;

    private AppDescriptor app2;

    private Extension ext1;

    private Extension ext2;

    private Extension ext3;

    private Extension ext4;

    private Extension ext5;

    private Location location1;

    private Location location2;
    
    private AppFrameworkServiceImpl service;

    private LoginLocationFilter loginLocationFilter;
    
    private PatientProgram patientProgram;
    
    private Program program;

	private ProgramWorkflowState state;

	private ProgramWorkflow workflow;
    
    private static final String PATIENT_UUID = "0cbe2ed3-cd5f-4f46-9459-26127c9265ab";


    @Before
    public void setUp() throws Exception {
        loginLocationFilter = new LoginLocationFilter() {
            @Override
            public boolean accept(Location location) {
                return true;
            }
        };

        PowerMockito.mockStatic(Context.class);
        when(Context.getRegisteredComponents(eq(LoginLocationFilter.class)))
                .thenReturn(Arrays.asList(loginLocationFilter));
        
    	setUpPatientProgram();
    	
    	allAppDescriptors = new AllAppDescriptors(validator);
    	allFreeStandingExtensions = new AllFreeStandingExtensions(validator);

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

        location1 = new Location();
        location2 = new Location();

        // populate login locations with data
        location1.setId(1);
        location1.setUuid("loginLocation1_uuid");
        location1.setName("loginLocation1");

        location2.setId(2);
        location2.setUuid("loginLocation2_uuid");
        location2.setName("loginLocation2");

        // add the login locations
        allLoginLocations.add(location1);
        allLoginLocations.add(location2);

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
    	
    	service = new AppFrameworkServiceImpl(null, allAppDescriptors, allFreeStandingExtensions, allComponentsState, null, featureToggles, appFrameworkConfig, null, allLoginLocations);
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

    @Test
    public void testGetLoginLocationsShouldReturnAllLoginLocations() throws Exception {
        // setup
        List<Location> loginLocations = allLoginLocations.getLoginLocations();

        // replay
        List<Location> actualLoginLocations = service.getLoginLocations();

        // verify
        assertEquals(loginLocations.size(), actualLoginLocations.size());
        assertEquals(loginLocations.get(0).getId(), actualLoginLocations.get(0).getId());
        assertEquals(loginLocations.get(0).getName(), actualLoginLocations.get(0).getName());
        assertEquals(loginLocations.get(0).getUuid(), actualLoginLocations.get(0).getUuid());
        assertEquals(loginLocations.get(1).getId(), actualLoginLocations.get(1).getId());
        assertEquals(loginLocations.get(1).getName(), actualLoginLocations.get(1).getName());
        assertEquals(loginLocations.get(1).getUuid(), actualLoginLocations.get(1).getUuid());
    }

    public void testGetPatientUuid() throws ScriptException {
    	// Setup
    	AppContextModel contextModel = new AppContextModel();
    	contextModel.put("patient", new PatientContextModel(PATIENT_UUID));
    	
    	// Replay
        String uuid = service.getPatientUuid(contextModel);
        
        // Verify
        assertEquals(PATIENT_UUID, uuid);
    }
    
    @Test
    public void testCheckRequiredProgramsOnCurrentPatient() {
    	// Setup
    	mockStatic(Context.class);
    	Patient patient = new Patient();
    	PatientService patientService = mock(PatientService.class);
    	ProgramWorkflowService workflowService = mock(ProgramWorkflowService.class);
    	
    	when(patientService.getPatientByUuid(PATIENT_UUID)).thenReturn(patient);
    	when(workflowService.getPatientPrograms(patient, null, null,
			    null, null, null, false)).thenReturn(Arrays.asList(patientProgram));
    	when(Context.getPatientService()).thenReturn(patientService);
    	when(Context.getProgramWorkflowService()).thenReturn(workflowService);
    	
    	AppContextModel contextModel = new AppContextModel();
    	contextModel.put("patient", new PatientContextModel(PATIENT_UUID));	
    	ProgramConfiguration config = new ProgramConfiguration();
    	config.setResolvedConfig(new ResolvedConfiguration(program, workflow, state));
    	
    	// Replay
    	boolean hasRequiredPrograms = service.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(config)), contextModel);
    	
    	// Verify
    	assertTrue(hasRequiredPrograms);
    	
    	// Re-setup
    	config.setResolvedConfig(new ResolvedConfiguration(null, workflow, state));
    	
    	// Replay
    	hasRequiredPrograms = service.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(config)), contextModel);
    	
    	// Verify
    	assertTrue(hasRequiredPrograms);
    	
    	// Re-setup
    	// Configuration with a state that's not a member of the patient's current states
    	ProgramWorkflowState pastState = new ProgramWorkflowState();
    	workflow.addState(pastState);
    	config.setResolvedConfig(new ResolvedConfiguration(null, null, pastState));
    	
    	// Replay
    	hasRequiredPrograms = service.checkRequiredProgramsOnCurrentPatient(extensionRequiringProgramConfigurations(Arrays.asList(config)), contextModel);
    	
    	// Verify
    	assertFalse(hasRequiredPrograms);
    }
    
    @Test
    public void hasProgramAssignableToConfiguration_shouldReturnTrueIfConfigIsAssignableToAnyOfThePatientPrograms() throws ScriptException {
    	// Setup
    	PatientProgram emptyPatientProgram = new PatientProgram();
    	emptyPatientProgram.setProgram(new Program());
    	List<PatientProgram> patientPrograms = Arrays.asList(emptyPatientProgram, patientProgram);
    	
    	ProgramConfiguration config = new ProgramConfiguration();
    	config.setResolvedConfig(new ResolvedConfiguration(program, workflow, state));

    	// Replay
        boolean isAssignable = service.hasProgramAssignableToConfiguration(patientPrograms, config);
        
        // Verify
        assertTrue(isAssignable);
    }
    
    @Test
    public void hasProgramAssignableToConfiguration_shouldReturnFalseIfConfigIsNotAssignableToAnyOfThePatientPrograms() throws ScriptException {
    	// Setup
    	PatientProgram emptyPatientProgram = new PatientProgram();
    	emptyPatientProgram.setProgram(new Program());
    	List<PatientProgram> patientPrograms = Arrays.asList(emptyPatientProgram, patientProgram);
    	
    	ProgramWorkflowState pastState = new ProgramWorkflowState();
    	workflow.addState(pastState);
    	
    	// Configuration with a state that's not a member of the patient's current states
    	ProgramConfiguration config = new ProgramConfiguration();
    	config.setResolvedConfig(new ResolvedConfiguration(program, workflow, pastState));
    	
    	// Replay
        boolean isAssignable = service.hasProgramAssignableToConfiguration(patientPrograms, config);
        
        // Verify
        assertFalse(isAssignable);
    }
    
    @Test
    public void hasProgramAssignableToConfiguration_shouldFailWithAnExceptionIfConfigurationHasAnInvalidProgramTree() {
    	// Setup
    	
    	// Configuration with invalid program tree
    	ProgramConfiguration config = new ProgramConfiguration();
    	config.setResolvedConfig(new ResolvedConfiguration(program, workflow, new ProgramWorkflowState()));
    	
    	try {
    		// Replay
    		service.hasProgramAssignableToConfiguration(Arrays.asList(patientProgram), config);
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
    
    private void setUpPatientProgram() {  	
    	program = new Program();
    	program.setUuid("588a31bb-7923-4ef8-a6fc-b8f2ae5d1343");
    	
    	state = new ProgramWorkflowState();
    	state.setUuid("588a31bb-7923-4ef8-a6fc-b8f2ae5d1344");
    	
    	workflow = new ProgramWorkflow();
    	workflow.setUuid("588a31bb-7923-4ef8-a6fc-b8f2ae5d1345");
    	
    	workflow.addState(state);
    	program.addWorkflow(workflow);
    	
    	patientProgram = mock(PatientProgram.class);
    	when(patientProgram.getProgram()).thenReturn(program);
    	PatientState currentState = new PatientState();
    	currentState.setState(state);
    	when(patientProgram.getCurrentStates()).thenReturn(new HashSet(Arrays.asList(currentState)));
    }
}
