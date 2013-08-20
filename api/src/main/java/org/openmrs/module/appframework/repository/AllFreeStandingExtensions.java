package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class represent all <em>free-standing</em> extensions, though it does not include extensions that are included in
 * apps found in {@link AllAppDescriptors}
 */
@Repository
public class AllFreeStandingExtensions {
	
	private List<Extension> extensions = new ArrayList<Extension>();
	
	private Validator validator;
	
	@Autowired
	public AllFreeStandingExtensions(Validator validator) {
		this.validator = validator;
	}
	
	public void add(List<Extension> extensions) {
		for (Extension extension : extensions) {
			add(extension);
		}
	}
	
	public void add(Extension extension) {
		validate(extension);
		
		synchronized (this.extensions) {
			this.extensions.add(extension);
			Collections.sort(this.extensions);
		}
	}
	
	private void validate(Extension extension) {
		Set<ConstraintViolation<Extension>> constraintViolations = validator.validate(extension);
		if (!constraintViolations.isEmpty())
			throw new IllegalArgumentException(constraintViolations.iterator().next().getMessage());
		
		if (this.extensions.contains(extension))
			throw new IllegalArgumentException("Extension already exists: " + extension.getId());
	}
	
	public List<Extension> getExtensions() {
		List<Extension> extensionList = new ArrayList<Extension>();
		extensionList.addAll(this.extensions);
		return extensionList;
	}
	
	public void clear() {
		extensions.clear();
	}
}
