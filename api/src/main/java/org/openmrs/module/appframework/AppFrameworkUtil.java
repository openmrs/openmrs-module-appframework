package org.openmrs.module.appframework;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.context.Context;

public class AppFrameworkUtil {

    private final static Log log = LogFactory.getLog(AppFrameworkUtil.class);

	/**
	 * Get the concept by id where the id can either be:
	 *   1) an integer id like 5090
	 *   2) a mapping type id like "XYZ:HT"
	 *   3) a uuid like "a3e12268-74bf-11df-9768-17cfc9833272"
	 *   4) a name
	 *
	 * @param id the concept identifier
	 * @return the concept if exist, else null
	 * @should find a concept by its conceptId
	 * @should find a concept by its mapping
	 * @should find a concept by its uuid
	 * @should find a concept by its name
	 * @should return null otherwise
	 */
    // NOTE: This method is a copy and paste from the htmlformentry module I guess it
	//  	should be deprecated on fixing: https://issues.openmrs.org/browse/TRUNK-5655
	public static Concept getConcept(String id) {
		if (StringUtils.isNotBlank(id)) {
			id = id.trim();
			// see if this is parseable to an Integer; if so, try looking up concept by id
			try { 
				// handle integer: id
				int conceptId = Integer.parseInt(id);
				return Context.getConceptService().getConcept(conceptId);
			} catch (Exception ex) {
				// pass
			}
			// handle mapping id: xyz:ht
			if (id.indexOf(":") != -1) {
				String [] sourceCodeSplit = id.split(":", 2);
				String source = sourceCodeSplit[0].trim();
				String term = sourceCodeSplit[1].trim();
				return Context.getConceptService().getConceptByMapping(term, source, false);
			}
			// handle name
			Concept ret = Context.getConceptService().getConceptByName(id);
			if (ret != null) {
				return ret;
			}
			// handle uuid 
			return Context.getConceptService().getConceptByUuid(id);
		}
		return null;
	}
	
	/**
	 * Gets <code>workflows</code> associated with a given <code>concept</code>
	 */
	public static List<ProgramWorkflow> getWorkflowsByConcept(Concept concept) {
	  List<ProgramWorkflow> ret = new ArrayList<ProgramWorkflow>();
	  for (ProgramWorkflow candidate : getAllWorkflows()) {
	      if (candidate.getConcept().equals(concept)) {
	        ret.add(candidate);
	      }
	  }
		return ret;
	}
	
	/**
	 * Gets <code>states</code> associated with a given <code>concept</code>
	 */
	public static List<ProgramWorkflowState> getStatesByConcept(Concept concept) {
	  List<ProgramWorkflowState> ret = new ArrayList<ProgramWorkflowState>();
	  for (ProgramWorkflow workflow : getAllWorkflows()) {
          for (ProgramWorkflowState candidate : workflow.getStates()) {
              if (candidate.getConcept().equals(concept)) {
                ret.add(candidate);
              }
          }
      }
		return ret;
	}
	
	/**
	 * Gets <code>programs</code> associated with a given <code>concept</code>
	 */
	public static List<Program> getProgramsByConcept(Concept concept) {
		List<Program> ret = new ArrayList<Program>();
		List<Program> allPrograms = Context.getProgramWorkflowService().getAllPrograms(false);
		if (CollectionUtils.isNotEmpty(allPrograms)) {
			for (Program candidate : allPrograms) {
				if (candidate.getConcept().equals(concept)) {
					ret.add(candidate);
				}
			}
		}
		return ret;
	}
	
	public static List<ProgramWorkflow> getAllWorkflows() {
	  List<ProgramWorkflow> ret = new ArrayList<ProgramWorkflow>();
	  List<Program> allPrograms = Context.getProgramWorkflowService().getAllPrograms(false);
	  for (Program program : allPrograms) {
	    Set<ProgramWorkflow> workflows = program.getAllWorkflows();
	    if (CollectionUtils.isNotEmpty(workflows)) {
	      ret.addAll(workflows);
	    }
	     
	  }
	  return ret;
	}

}