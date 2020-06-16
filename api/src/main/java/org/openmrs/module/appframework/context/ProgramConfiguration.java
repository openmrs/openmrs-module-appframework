package org.openmrs.module.appframework.context;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openmrs.Concept;
import org.openmrs.OpenmrsObject;
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
		List<Program> programs = this.getAllPossiblePrograms();
		List<ProgramWorkflow> workflows = this.getAllPossibleWorkflows();
		List<ProgramWorkflowState> states = this.getAllPossibleStates();
		
		if (CollectionUtils.isNotEmpty(programs) && CollectionUtils.isNotEmpty(workflows)) {
			filter(programs, workflows);
		}
		if (CollectionUtils.isNotEmpty(workflows) && CollectionUtils.isNotEmpty(states)) {
			filter(workflows, states);
		}
		if (CollectionUtils.isNotEmpty(programs) && CollectionUtils.isNotEmpty(states)) {
			if (!(programs.size() == 1 && states.size() == 1)) {
				filter(programs, states);
			}
		}
		// Collect short listed items
		ResolvedConfiguration ret = new ResolvedConfiguration();
		if (programs != null) {
			if (programs.size() == 1) {
				ret.setProgram(programs.get(0));
			} else {
				throw new APIException("Failed to figure out the intended program identified by: " + programRef);
			}
		}
		if (workflows != null) {
			if (workflows.size() == 1) {
				ret.setWorkflow(workflows.get(0));
			} else {
				throw new APIException("Failed to figure out the intended workflow identified by: " + workflowRef);
			}
		}
		if (states != null) {
			if (states.size() == 1) {
				ret.setState(states.get(0));
			} else {
				throw new APIException("Failed to figure out the intended state identified by: " + stateRef);				
			}
		}
	    return ret;
	}
	
	/**
	 * Combines member(s) from the owners and owned list and retains only those that make a valid tree. 
	 * 
	 * <p>
	 * A valid tree is deduced when an {@code Owner} has(owns) the {@code Owned} member it's combined with.
	 * Eg. If a {@code Program}(owner) is combined with a {@code ProgramWorkflow}(owned) and the workflow is a member
	 * of the program's workflows.
	 * 
	 * @param <Owner> In the ProgramConfiguration context, an owner is an {@link OpenmrsObject} that has a workflow or 
	 * 				  state eg. {@code Program}
	 * @param <Owned> In the ProgramConfiguration context, an owned item is an {@link OpenmrsObject} that is part of a 
	 * 				  program or workflow eg. {@code ProgramWorkflowState}
	 * @param owners the owners
	 * @param theOwned the owned
	 */
	private <Owner, Owned> void filter(List<Owner> owners, List<Owned> theOwned) {	
	    List<Owner> ownersFiltrate = new ArrayList<Owner>();
	    List<Owned> ownedMembersFiltrate = new ArrayList<Owned>();
	    List<AbstractMap.SimpleEntry<Owned, Owner>> ownedToOwnersMapEntries = new ArrayList<AbstractMap.SimpleEntry<Owned, Owner>>();

	    for (Owner owner : owners) {
	    	for (Owned owned : theOwned) {
	    		AbstractMap.SimpleEntry<Owned, Owner> entry = new AbstractMap.SimpleEntry<Owned, Owner>(owned, owner);
	    		ownedToOwnersMapEntries.add(entry);
	    	}
	    }    
	    for (Entry<Owned, Owner> entry : ownedToOwnersMapEntries) {
	    	Owner owner = entry.getValue();
	    	Owned owned = entry.getKey();
	    	if (owner instanceof Program) {
	    		Set<ProgramWorkflow> workflows = ((Program)owner).getAllWorkflows();
	    		if (owned instanceof ProgramWorkflow) {
	    			if (workflows.contains(owned)) {
	    				ownersFiltrate.add(owner);
	    				ownedMembersFiltrate.add(owned);
	    			}
	    		} else if (owned instanceof ProgramWorkflowState) {
	    			for (ProgramWorkflow workflow : workflows) {
	    				if (workflow.getStates(false).contains(owned)) {
	    					ownersFiltrate.add(owner);
		    				ownedMembersFiltrate.add(owned);
	    				}
	    			}
	    		}
	    	} else if (owner instanceof ProgramWorkflow){
	    		if (((ProgramWorkflow)owner).getStates(false).contains(owned)) {
	    			ownersFiltrate.add(owner);
    				ownedMembersFiltrate.add(owned);
	    		}
	    	}
	    }
	    // update the short lists
	    owners.retainAll(ownersFiltrate);
	    theOwned.retainAll(ownedMembersFiltrate);
	}
		
	/**
	 * Gets program(s) identified by the {@link #programRef}
	 * 
	 * @should get program by it's {@code id}
	 * @should get program by it's {@code uuid} 
	 * @should get program(s) by the associated {@code concept}
	 */
	private List<Program> getAllPossiblePrograms() {
		if (programRef == null) {
			return null;
		}
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
		if (workflowRef == null) {
			return null;
		}
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
		if (stateRef == null) {
			return null;
		}
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