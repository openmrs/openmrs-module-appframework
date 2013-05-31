package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
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

    /**
     * Sets the transient {@link AppDescriptor#template} field on apps, based on {@link AppDescriptor#instanceOf}
     * @param allAppTemplates
     */
    public void setAppTemplatesOnInstances(AllAppTemplates allAppTemplates) {
        for (AppDescriptor appDescriptor : appDescriptors) {
            String inheritsFromTemplateId = appDescriptor.getInstanceOf();
            if (inheritsFromTemplateId != null) {
                AppTemplate template = allAppTemplates.getAppTemplate(inheritsFromTemplateId);
                if (template == null) {
                    throw new IllegalStateException("App '" + appDescriptor.getId() + "' says its an instanceOf '" + inheritsFromTemplateId + "' but there is no AppTemplate with that id");
                } else {
                    appDescriptor.setTemplate(template);
                }
            }
        }
    }

    /**
     * Gets an app by its id
     * @param id
     * @return
     */
    public AppDescriptor getAppDescriptor(String id) {
        for (AppDescriptor candidate : appDescriptors) {
            if (candidate.getId().equals(id)) {
                return candidate;
            }
        }
        return null;

    }

    /**
     * Sets the {@link Extension#belongsTo} field on each extension that belongs to an app
     */
    public void setExtensionApps() {
        for (AppDescriptor appDescriptor : appDescriptors) {
            if (appDescriptor.getExtensions() != null) {
                for (Extension extension : appDescriptor.getExtensions()) {
                    extension.setBelongsTo(appDescriptor);
                }
            }
        }
    }

}
