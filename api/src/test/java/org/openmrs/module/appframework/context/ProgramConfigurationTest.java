package org.openmrs.module.appframework.context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.APIException;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppFrameworkUtil;
import org.openmrs.module.appframework.context.ProgramConfiguration.ResolvedConfiguration;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, AppFrameworkUtil.class})
public class ProgramConfigurationTest {

	ProgramConfiguration configuration;
	
	// Concept associated with programs
	Concept concept1;
	
	// Concept associated with workflows
	Concept concept2;
	
	// Concepts associated with states
	@Mock
	Concept concept3;
	
	@Mock
	Concept concept4;
	
	@Mock
	ProgramWorkflowService service;
		
	Program program1;
	
	Program program2;
	
	ProgramWorkflow workflow1;
	
	ProgramWorkflow workflow2;
	
	ProgramWorkflowState state1;
	
	ProgramWorkflowState state2;
	
	List<Program> programsSharingConcept;
	
	List<ProgramWorkflow> workflowsSharingConcept;
	
	List<ProgramWorkflowState> statesSharingConcept;
	
	private static final String PROGRAM_CONCEPT = "CIEL:123";
	
	private static final String WORKFLOW_CONCEPT = "CIEL:124";
	
	private static final String STATE_CONCEPT = "CIEL:125";
	
	@Before
	public void setup() {
		setupPrograms();
		configuration = new ProgramConfiguration(PROGRAM_CONCEPT, WORKFLOW_CONCEPT, STATE_CONCEPT);
		mockStatic(AppFrameworkUtil.class);
		mockStatic(Context.class);
		
		when(AppFrameworkUtil.getConcept(PROGRAM_CONCEPT)).thenReturn(concept1);
		when(AppFrameworkUtil.getConcept(WORKFLOW_CONCEPT)).thenReturn(concept2);
		when(AppFrameworkUtil.getConcept(STATE_CONCEPT)).thenReturn(concept3);
		when(Context.getProgramWorkflowService()).thenReturn(service);
		when(service.getProgram(any(Integer.class))).thenReturn(null);
		when(service.getProgramByUuid(any(String.class))).thenReturn(null);
		when(service.getWorkflow(any(Integer.class))).thenReturn(null);
		when(service.getWorkflowByUuid(any(String.class))).thenReturn(null);
		when(service.getState(any(Integer.class))).thenReturn(null);
		when(service.getStateByUuid(any(String.class))).thenReturn(null);
		
		programsSharingConcept = new ArrayList<Program>(Arrays.asList(program1, program2));
		workflowsSharingConcept = new ArrayList<ProgramWorkflow>(Arrays.asList(workflow1, workflow2));
		statesSharingConcept = new ArrayList<ProgramWorkflowState>(Arrays.asList(state1, state2));
		
		when(AppFrameworkUtil.getProgramsByConcept(concept1)).thenReturn(programsSharingConcept);
		when(AppFrameworkUtil.getWorkflowsByConcept(concept2)).thenReturn(workflowsSharingConcept);
		when(AppFrameworkUtil.getStatesByConcept(concept3)).thenReturn(statesSharingConcept);
	}
	
	@Test
	public void getProgram_shouldHandleCasesWhereUnderlyingProgramIsObvious() {
		// Setup
		when(AppFrameworkUtil.getProgramsByConcept(concept1)).thenReturn(new ArrayList<Program>(Arrays.asList(program1)));
		
		// Replay
		Program program = configuration.getProgram();
		
		// Verify
		Assert.assertEquals(program1, program);
	}
	
	@Test
	public void getProgram_shouldHandleCasesWhereUnderlyingProgramIsNotObvious() {
		// Setup
		when(AppFrameworkUtil.getStatesByConcept(concept3)).thenReturn(new ArrayList<ProgramWorkflowState>(Arrays.asList(state1)));
		
		// Replay
		Program program = configuration.getProgram();
		
		// Verify
		Assert.assertEquals(program1, program);
	}
	
	@Test
	public void getProgram_shouldFailIfProgramCanNotBeChose() {
		try {			
			// Replay
			configuration.getProgram();
			fail("Should have failed since we have two programs, states and workflows identified by the smae concept");
		} catch(APIException e) {	
			// Verify
			Assert.assertEquals("Failed to figure out the intended program identified by: " 
					+ PROGRAM_CONCEPT, e.getMessage());
		}
	}
	
	@Test
	public void getWorkflow_shouldHandleCasesWhereUnderlyingWorkflowIsObvious() {
		// Setup
		when(AppFrameworkUtil.getWorkflowsByConcept(concept2)).thenReturn(new ArrayList<ProgramWorkflow>(Arrays.asList(workflow1)));
		
		// Replay
		ProgramWorkflow worklow = configuration.getWorkflow();
		
		// Verify
		Assert.assertEquals(workflow1, worklow);
	}
	
	@Test
	public void getWorkflow_shouldHandleCasesWhereUnderlyingWorkflowIsNotObvious() {
		// Setup
		when(AppFrameworkUtil.getStatesByConcept(concept3)).thenReturn(new ArrayList<ProgramWorkflowState>(Arrays.asList(state1)));
		
		// Replay
		ProgramWorkflow worklow = configuration.getWorkflow();
		
		// Verify
		Assert.assertEquals(workflow1, worklow);
	}
	
	@Test
	public void getWorkflow_shouldFailIfWorkflowCanNotBeChose() {	
		try {
			// Replay
			configuration.getWorkflow();
			fail("Should have failed at the program level since we have more than one program, workflow and states identified by the"
					+ " same concept.");
		} catch(APIException e) {
			// Verify
			Assert.assertEquals("Failed to figure out the intended program identified by: " 
					+ PROGRAM_CONCEPT, e.getMessage());
		}
		
		// Re-setup
		configuration = new ProgramConfiguration(null, WORKFLOW_CONCEPT, null);
		try {
			// Replay
			configuration.getWorkflow();
			fail("Should have failed at the workflow level or base level since we have more than one workflow identified by the same"
					+ " concept yet this config has no program or state specified.");
		} catch(APIException e) {
			// Verify
			Assert.assertEquals("Failed to figure out the intended workflow identified by: " 
					+ WORKFLOW_CONCEPT, e.getMessage());	
		}
	}
	
	@Test
	public void getState_shouldHandleCasesWhereUnderlyingStateIsObvious() {
		// Setup
		when(AppFrameworkUtil.getStatesByConcept(concept3)).thenReturn(new ArrayList<ProgramWorkflowState>(Arrays.asList(state1)));

		// Replay
		ProgramWorkflowState state = configuration.getState();
		
		// Verify
		Assert.assertEquals(state1, state);
	}
	
	@Test
	public void getState_shouldHandleCasesWhereUnderlyingStateIsNotObvious() {
		// Setup
		when(AppFrameworkUtil.getProgramsByConcept(concept1)).thenReturn(new ArrayList<Program>(Arrays.asList(program1)));
		
		// Replay
		ProgramWorkflowState state = configuration.getState();
		
		// Verify
		Assert.assertEquals(state1, state);
	}
	
	@Test
	public void getState_shouldFailIfStateCanNotBeChose() {		
		try {
			// Replay
			configuration.getState();
			fail("Should have failed at the program level since we have more than one program, workflow and states "
					+ "identified by the same concept");
		} catch(APIException e) {
			// Verify
			Assert.assertEquals("Failed to figure out the intended program identified by: " 
					+ PROGRAM_CONCEPT, e.getMessage());
		}
		
		// Re-setup
		configuration = new ProgramConfiguration(null, null, STATE_CONCEPT);
		
		try {
			// Replay
			configuration.getState();
			fail("Should have faied at the state level or base level since we have more than one state identified "
					+ "by the same  concept yet this config has no program or workflow specified.");
		} catch(APIException e) {
			// Verify
			Assert.assertEquals("Failed to figure out the intended state identified by: " 
					+ STATE_CONCEPT, e.getMessage());	
		}		
	}
	
	@Test
	public void hasValidProgramTree_shouldReturnTrueIfTreeIsValid() {
		// setup
		// program + workflow + state
		ResolvedConfiguration resolvedConfig = new ResolvedConfiguration(program1, workflow1, state1);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertTrue(configuration.hasValidProgramTree());
		
		// re-setup
		// program + state 
		resolvedConfig = new ResolvedConfiguration(program1, null, state1);
		
		// replay and verify
		Assert.assertTrue(configuration.hasValidProgramTree());
				
		// re-setup
		// program + workflow 
		resolvedConfig = new ResolvedConfiguration(program1, workflow1, null);
		
		// replay and verify
		Assert.assertTrue(configuration.hasValidProgramTree());
	}
	
	@Test
	public void hasValidProgramTree_shouldReturnTrueIfWeHaveNoTree() {
		// setup
		// program only
		ResolvedConfiguration resolvedConfig = new ResolvedConfiguration(program1, null, null);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertTrue(configuration.hasValidProgramTree());
		
		// setup
		// workflow only
		resolvedConfig = new ResolvedConfiguration(null, workflow1, null);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertTrue(configuration.hasValidProgramTree());

		// setup
		// state only
		resolvedConfig = new ResolvedConfiguration(null, null, state1);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertTrue(configuration.hasValidProgramTree());
	}
	
	@Test
	public void hasValidProgramTree_shouldReturnFalseIfTreeIsInvalid() {
		// setup
		// program + workflow of different program + state
		ResolvedConfiguration resolvedConfig = new ResolvedConfiguration(program1, workflow2, state1);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertFalse(configuration.hasValidProgramTree());

		// re-setup
		// program + workflow + state of different program/workflow
		resolvedConfig = new ResolvedConfiguration(program1, workflow1, state2);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertFalse(configuration.hasValidProgramTree());

		// re-setup
		// program + state of different program
		resolvedConfig = new ResolvedConfiguration(program1, null, state2);
		configuration.setResolvedConfig(resolvedConfig);
		
		// replay and verify
		Assert.assertFalse(configuration.hasValidProgramTree());	
	}
	
	private void setupPrograms() {
		setupConcepts();
		
		state1 = new ProgramWorkflowState(1);
		state2 = new ProgramWorkflowState(2);
		
		state1.setConcept(concept3);
		state2.setConcept(concept4);
		
		workflow1 = new ProgramWorkflow(1);
		workflow2 = new ProgramWorkflow(2);
		
		program1 = new Program(1);
		program2 = new Program(2);
		
		program1.addWorkflow(workflow1);
		program2.addWorkflow(workflow2);
		
		workflow1.addState(state1);
		workflow2.addState(state2);
	}
	
	private void setupConcepts() {
		concept1 = new Concept(1);
		concept2 = new Concept(2);	
		when(concept3.getName()).thenReturn(new ConceptName("State One", null));
		when(concept4.getName()).thenReturn(new ConceptName("State Two", null));
	}
}
