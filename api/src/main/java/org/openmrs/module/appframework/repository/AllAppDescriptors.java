package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class AllAppDescriptors {
	
	private List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();
	
	private Validator validator;
	
	@Autowired
	public AllAppDescriptors(Validator validator) {
		this.validator = validator;
	}
	
	public void add(List<AppDescriptor> appDescriptors) {
		for (AppDescriptor appDescriptor : appDescriptors) {
			add(appDescriptor);
		}
	}
	
	public void add(AppDescriptor appDescriptor) {
		validate(appDescriptor);
		
		// Since the repository is an in-memory list, it has to be
		// protected against multiple threads.
		synchronized (appDescriptors) {
			this.appDescriptors.add(appDescriptor);
			Collections.sort(this.appDescriptors);
		}
	}
	
	private void validate(AppDescriptor appDescriptor) {
		Set<ConstraintViolation<AppDescriptor>> constraintViolations = validator.validate(appDescriptor);
		if (!constraintViolations.isEmpty())
			throw new IllegalArgumentException(constraintViolations.iterator().next().getMessage());
		
		if (this.appDescriptors.contains(appDescriptor))
			throw new IllegalArgumentException("App already exists: " + appDescriptor.getId());
	}
	
	public List<AppDescriptor> getAppDescriptors() {
		List<AppDescriptor> appDescriptorList = new ArrayList<AppDescriptor>();
		appDescriptorList.addAll(this.appDescriptors);
		return appDescriptorList;
	}
	
	public void clear() {
		appDescriptors.clear();
	}
	
}
