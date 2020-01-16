package org.openmrs.module.appframework;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class AppFrameworkUtilTest {

	@Mock
	ConceptService conceptService;
	
	@Mock 
	ProgramWorkflowService programWorkflowService;
		
	Concept concept;
	
	@Before
	public void setup() {
		concept = new Concept(1);
		
		mockStatic(Context.class);
		when(Context.getConceptService()).thenReturn(conceptService);
		when(Context.getProgramWorkflowService()).thenReturn(programWorkflowService);
		when(programWorkflowService.getAllPrograms(false)).thenReturn(setupPrograms());
	
	}

	@Test
	public void getProgramsByConcept_shouldGetAllProgramsIdentifiedByGivenConcept() {		
		// Replay
		List<Program> programsIdentifiedByConcept = AppFrameworkUtil.getProgramsByConcept(concept);
		
		// Verify
		Assert.assertThat(programsIdentifiedByConcept.size(), is(2));

	}
	
	@Test
	public void getAllWorkflows_shouldReturnAllWorkflows() {
		// Replay
		List<ProgramWorkflow> workflows = AppFrameworkUtil.getAllWorkflows();
		
		// Verify
		Assert.assertThat(workflows.size(), is(4));
	}
	
	@Test
	public void getWorkflowsByConcept_shouldGetAllWorkflowsIdentifiedByConcept() {
		// Replay
		List<ProgramWorkflow> workflows = AppFrameworkUtil.getWorkflowsByConcept(concept);
		
		// Verify
		Assert.assertThat(workflows.size(), is(2));
	}
	
	@Test
	public void getStatesByConcept_shouldGetAllStatesIdentifiedByConcept() {
		// Replay
		List<ProgramWorkflowState> states = AppFrameworkUtil.getStatesByConcept(concept);
		
		// Verify
		Assert.assertThat(states.size(), is(1));
	}
	
	private List<Program> setupPrograms() {
		Program program1 = new Program();
		program1.setConcept(new Concept());
		Program program2 = new Program();
		program2.setConcept(concept);
		Program program3 = new Program();
		program3.setConcept(concept);
		
		ProgramWorkflow workflow1 = new ProgramWorkflow();
		ProgramWorkflowState state1 = new ProgramWorkflowState();
		state1.setConcept(new Concept(8));
		workflow1.addState(state1);
		workflow1.setConcept(concept);
		program1.addWorkflow(workflow1);
		
		ProgramWorkflow workflow2 = new ProgramWorkflow();
		ProgramWorkflowState state2 = new ProgramWorkflowState();
		state2.setConcept(concept);
		workflow2.addState(state2);
		workflow2.setConcept(new Concept(3));
		program1.addWorkflow(workflow2);
		
		ProgramWorkflow workflow3 = new ProgramWorkflow();
		ProgramWorkflowState state3 = new ProgramWorkflowState();
		state3.setConcept(new Concept(9));
		workflow3.addState(state3);
		workflow3.setConcept(new Concept(7));
		program3.addWorkflow(workflow3);
		
		ProgramWorkflow workflow4 = new ProgramWorkflow();
		ProgramWorkflowState state4 = new ProgramWorkflowState();
		state4.setConcept(new Concept(10));
		workflow4.addState(state4);
		workflow4.setConcept(concept);
		program3.addWorkflow(workflow4);
		
		return Arrays.asList(program1, program2, program3);
	}
	
}
