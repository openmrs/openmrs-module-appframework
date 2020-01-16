package org.openmrs.module.appframework.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppFrameworkUtil;

public class ProgramConfiguration {
	
	@JsonProperty
    private String programRef;
	
	@JsonProperty
    private String workflowRef;
	
	@JsonProperty
    private String stateRef;
	
	private ResolvedConfiguration resolvedConfig;
		
	private List<ProgramWorkflow> allPossibleWorkflows;
	
	private List<ProgramWorkflowState> allPossibleStates;
	
    public ProgramConfiguration() {
    	
    }
    
	public ProgramConfiguration(String programRef, String workflowRef, String stateRef) {
		this.programRef = programRef;
		this.workflowRef = workflowRef;
		this.stateRef = stateRef;
	}
	
	public Program getProgram() {
		if (resolvedConfig == null) {
			resolvedConfig = getResolvedConfig();
		}
		return resolvedConfig.getProgram();
	}
	
	public ProgramWorkflow getWorkflow() {
		if (resolvedConfig == null) {
			resolvedConfig = getResolvedConfig();
		}
		return resolvedConfig.getWorkflow();
	}
	
	public ProgramWorkflowState getState() {
		if (resolvedConfig == null) {
			resolvedConfig = getResolvedConfig();
		}
		return resolvedConfig.getState();
	}
	
	/**
	 * Checks whether the underlying {@code program}, {@code workflow} or {@code state} are under the same program tree
	 * 
	 * <p>
	 *  For instance if a {@link ProgramConfiguration} has a {@code resolvedConfig} with a {@code program}, {@code workflow}
	 *  and {@code state}; this method determines whether the {@code state} is associated with the {@code workflow} and if
	 *  the {@code workflow} is also associated with the {@code program}.
	 * 
	 * @return {@code true} if a valid program tree was found
	 */
	public boolean hasValidProgramTree() {
		Program program = getProgram();
		ProgramWorkflow workflow = getWorkflow();
		ProgramWorkflowState state = getState();
		// Only program was specified
		if (program != null && workflow == null && state == null) {
			return true;			
		}
		// Only workflow was specified
		if (program == null && workflow != null && state == null) {
			return true;			
		}
		// Only state was specified
		if (program == null && workflow == null && state != null) {
			return true;			
		}
		// For cases where only workflow and state were specified, be sure the state belongs to the specified workflow
		if (program == null && workflow != null && state != null) {
			return workflow.getStates().contains(state);
		}
		// For cases where only program and state were specified, be sure the state belongs to a workflow associated with program
		if (program != null && workflow == null && state != null) {
			boolean stateInProgramTree = false;
			for (ProgramWorkflow candidate : program.getAllWorkflows()) {
				if (candidate.getStates().contains(state)) {
					stateInProgramTree = true;
				}
			}
			return stateInProgramTree;
		}
		// For cases where a workflow and program were specified, be sure the workflow belongs to the specified program
		if (program != null && workflow != null) {
			boolean programHasWorkflow = program.getAllWorkflows().contains(workflow);
			if (state != null) {
				// For cases where a workflow, program and state were specified
				return programHasWorkflow && workflow.getStates().contains(state);
			}
			return programHasWorkflow;
		}
		return false;
	}
	
	protected ResolvedConfiguration getResolvedConfig() {
		if (resolvedConfig != null) {
			return resolvedConfig;
		}
		ResolvedConfiguration ret = new ResolvedConfiguration();	    
		if (StringUtils.isNotBlank(programRef)) {
		    List<Program> programs = getAllPossiblePrograms();
			if (programs.size() == 1) {
				ret.setProgram(programs.get(0));
			} else if (programs.size() > 1) {
				ret.setProgram(programWithBestWorflowAndStateCombination(programs));
			}
		}			    	    
		if (StringUtils.isNotBlank(workflowRef)) {
		    List<ProgramWorkflow> workflows = getAllPossibleWorkflows();
		    if (workflows.size() == 1) {
				ret.setWorkflow(workflows.get(0));
			} else if (workflows.size() > 1) {
				ret.setWorkflow(workflowWithBestProgramAndStateCombination(workflows, ret.getProgram()));
			} 
		}		
		if (StringUtils.isNotBlank(stateRef)) {
		    List<ProgramWorkflowState> states = getAllPossibleStates();
		    if (states.size() == 1) {
				ret.setState(states.get(0));
			} else if (states.size() > 1) {
				ret.setState(stateWithBestProgramAndWorkflowCombination(states, ret.getProgram(), ret.getWorkflow()));
			}
		}	    
	    return ret;
	}

	protected Program programWithBestWorflowAndStateCombination(List<Program> programsSharingConcept) {
		Set<Program> candidates = new HashSet<Program>();
		for (Program program : programsSharingConcept) {
			if (StringUtils.isNotBlank(workflowRef) && allPossibleWorkflows == null) {
				allPossibleWorkflows = getAllPossibleWorkflows();
			} 
			if (CollectionUtils.isNotEmpty(allPossibleWorkflows)) {
				for (ProgramWorkflow workflow : allPossibleWorkflows) {
					if (program.getAllWorkflows().contains(workflow)) {
						candidates.add(program);
					}
				}
			}
		}
		if (candidates.size() == 1) {
			return candidates.iterator().next();
		}		
		if (StringUtils.isNotBlank(stateRef) && candidates.size() > 1) {
			programsSharingConcept.clear();
			programsSharingConcept.addAll(candidates);
			candidates.clear();
			for (Program candidate : programsSharingConcept) {
				if (StringUtils.isNotBlank(stateRef) && allPossibleStates == null) {
					allPossibleStates = getAllPossibleStates();
				}
				for (ProgramWorkflowState state : allPossibleStates) {
					for (ProgramWorkflow workflow : candidate.getAllWorkflows()) {
						if (workflow.getSortedStates().contains(state)) {							
							candidates.add(candidate);
						}
					}
				}
			}
			if (candidates.size() == 1) {
				return candidates.iterator().next();
			}
		}
		// If we didn't get any qualifying candidate or got more than one
		throw new APIException("Could not choose the intended program out of the many programs identified by concept: " + programRef);
	}
	
	@SuppressWarnings("unchecked")
	protected ProgramWorkflow workflowWithBestProgramAndStateCombination(List<ProgramWorkflow> workflowsSharingConcept,
			Program underlyingProgram) {
		Collection<ProgramWorkflow> candidates = new HashSet<ProgramWorkflow>();
		// Look at the states and try to find out the best combination(s)
		if (StringUtils.isNotBlank(stateRef)) {
			for (ProgramWorkflow candidate : workflowsSharingConcept) {
				if (allPossibleStates == null) {
					allPossibleStates = getAllPossibleStates();
				}
				for (ProgramWorkflowState state : allPossibleStates) {
					if (candidate.getSortedStates().contains(state)) {
						candidates.add(candidate);
					}
				}
			}
		}
		if (candidates.size() == 1) {
			return candidates.iterator().next();
		} else if (candidates.size() > 1) {
			// Move up to the underlying program and see whether we can have an outstanding combination
			if (underlyingProgram != null) {
				candidates =  CollectionUtils.intersection(candidates, underlyingProgram.getAllWorkflows());
				if (candidates.size() == 1) {
					return candidates.iterator().next();
				}
			}
		}
		// If we didn't get any qualifying candidate or got more than one
		throw new APIException("Could not choose the intended workflow out of the many workflows identified by concept: " + workflowRef);
	}
	
	@SuppressWarnings("unchecked")
	protected ProgramWorkflowState stateWithBestProgramAndWorkflowCombination(List<ProgramWorkflowState> statesSharingConcept, 
			Program underlyingProgram, ProgramWorkflow underlyingWorkflow) {
		Collection<ProgramWorkflowState> candidates = new HashSet<ProgramWorkflowState>();
		// Try combining with the underlying workflow
		if (underlyingWorkflow != null) {
			candidates = CollectionUtils.intersection(statesSharingConcept, underlyingWorkflow.getSortedStates());
			if (candidates.size() == 1) {
				return candidates.iterator().next();
			}
		}
		// Try combining with the underlying program
		if (underlyingProgram != null) {
			Set<ProgramWorkflowState> allProgramStates = new HashSet<ProgramWorkflowState>();
			for (ProgramWorkflow workflow : underlyingProgram.getAllWorkflows()) {
				allProgramStates.addAll(workflow.getSortedStates());
			}
			if (CollectionUtils.isNotEmpty(candidates)) {
				candidates = CollectionUtils.intersection(candidates, allProgramStates);
			} else {
				candidates = CollectionUtils.intersection(statesSharingConcept, allProgramStates);
			}
			if (candidates.size() == 1) {
				return candidates.iterator().next();
			}
		}
		// If we didn't get any qualifying candidate or got more than one
		throw new APIException("Could not choose the intended state out of the many states identified by concept: " + stateRef);
	}
	
	/**
	 * Gets program(s) identified by the {@link #programRef}
	 * 
	 * @should get program by it's {@code id}
	 * @should get program by it's {@code uuid} 
	 * @should get program(s) by the associated {@code concept}
	 */
	private List<Program> getAllPossiblePrograms() {
		try {
			Integer programId = Integer.parseInt(programRef);
			Program program = Context.getProgramWorkflowService().getProgram(programId);
			if (program != null) {
				return Arrays.asList(program);
			}
		} catch (NumberFormatException e) {
			// try with the uuid
			Program program = Context.getProgramWorkflowService().getProgramByUuid(programRef);
			if (program != null) {
				return Arrays.asList(program);
			}
		}
		// try using the concept
		Concept concept = AppFrameworkUtil.getConcept(programRef);
		if (concept == null) {
    		throw new APIException("Could not find concept identified by: " + programRef);
		}
		List<Program> programs = AppFrameworkUtil.getProgramsByConcept(concept);
		if (programs.isEmpty()) {
    		throw new APIException("Could not find program(s) identified by concept: " + concept);
		}
		return programs;
	}
	
	/**
	 * Gets workflow(s) identified by the {@link #workflowRef}
	 * 
	 * @should get workflow by it's {@code id}
	 * @should get workflow by it's {@code uuid} 
	 * @should get workflow(s) by the associated {@code concept}
	 */
	private List<ProgramWorkflow> getAllPossibleWorkflows() {
		try {
			Integer workflowId = Integer.parseInt(workflowRef);
			ProgramWorkflow workflow = Context.getProgramWorkflowService().getWorkflow(workflowId);
			if (workflow != null) {
				return Arrays.asList(workflow);
			}
		} catch (NumberFormatException e) {
			// try with the uuid
			ProgramWorkflow workflow = Context.getProgramWorkflowService().getWorkflowByUuid(workflowRef);
			if (workflow != null) {
				return Arrays.asList(workflow);
			}
		}
		Concept workflowConcept = AppFrameworkUtil.getConcept(workflowRef);
		if (workflowConcept == null) {
    		throw new APIException("Could not find concept identified by: " + workflowRef);
		}
		List<ProgramWorkflow> workflows = AppFrameworkUtil.getWorkflowsByConcept(workflowConcept);
		if (CollectionUtils.isEmpty(workflows)) {
    		throw new APIException("Could not find workflow(s) identified by concept: " + workflowConcept);
		}
		return workflows;
	}
	
	/**
	 * Gets state(s) identified by the {@link #stateRef}
	 * 
	 * @should get state by it's {@code id}
	 * @should get state by it's {@code uuid} 
	 * @should get state(s) by the associated {@code concept}
	 */
	private List<ProgramWorkflowState> getAllPossibleStates() {
		try {
			Integer stateId = Integer.parseInt(stateRef);
			ProgramWorkflowState state = Context.getProgramWorkflowService().getState(stateId);
			if (state != null) {
				return Arrays.asList(state);
			}
		} catch (NumberFormatException e) {
			// try with the uuid
			ProgramWorkflowState state = Context.getProgramWorkflowService().getStateByUuid(stateRef);
			if (state != null) {
				return Arrays.asList(state);
			}
		}
		Concept stateConcept = AppFrameworkUtil.getConcept(stateRef);
		if (stateConcept == null) {
    		throw new APIException("Could not find concept identified by: " + stateRef);
		}
		List<ProgramWorkflowState> states = AppFrameworkUtil.getStatesByConcept(stateConcept);
		if (CollectionUtils.isEmpty(states)) {
    		throw new APIException("Could not find state(s) identified by concept: " + stateConcept);
		}
		return states;
	}
	
	public void setResolvedConfig(ResolvedConfiguration resolvedConfig) {
		this.resolvedConfig = resolvedConfig;
	}
	
	public boolean hasProgram() {
		return getResolvedConfig().getProgram() != null;
	}
	
	public boolean hasWorkflow() {
		return getResolvedConfig().getWorkflow() != null;
	}

	public boolean hasState() {
		return getResolvedConfig().getState() != null;
	}
	
	public boolean hasAll() {
		return hasProgram() && hasWorkflow() && hasState();
	}
	
	public static class ResolvedConfiguration {
		
		private Program program;
		
		private ProgramWorkflow workflow;
		
		private ProgramWorkflowState state;
		
		public ResolvedConfiguration() {
			
		}
		
		public ResolvedConfiguration(Program program, ProgramWorkflow workflow, ProgramWorkflowState state) {
			this.program = program;
			this.workflow = workflow;
			this.state = state;
		}
		
		public Program getProgram() {
			return program;
		}
		
		public void setProgram(Program program) {
			this.program = program;
		}
		
		public ProgramWorkflow getWorkflow() {
			return workflow;
		}
		
		public void setWorkflow(ProgramWorkflow workflow) {
			this.workflow = workflow;
		}
		
		public ProgramWorkflowState getState() {
			return state;
		}
		
		public void setState(ProgramWorkflowState state) {
			this.state = state;
		}
	}
	    
}