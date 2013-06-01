package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.AppTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Repository
public class AllAppTemplates {

    private List<AppTemplate> appTemplates = new ArrayList<AppTemplate>();

    @Autowired
    private Validator validator;

    public AllAppTemplates() {
    }

    public AllAppTemplates(Validator validator) {
        this.validator = validator;
    }

    public void add(Collection<AppTemplate> templates) {
        for (AppTemplate template : templates) {
            add(template);
        }
    }

    public void add(AppTemplate template) {
        validate(template);
        appTemplates.add(template);
    }

    public List<AppTemplate> getAppTemplates() {
        return Collections.unmodifiableList(appTemplates);
    }

    public void clear() {
        appTemplates.clear();
    }

    private void validate(AppTemplate appTemplate) {
        Set<ConstraintViolation<AppTemplate>> constraintViolations = validator.validate(appTemplate);
        if (!constraintViolations.isEmpty()) {
            throw new IllegalArgumentException(constraintViolations.iterator().next().getMessage());
        }

        if (this.appTemplates.contains(appTemplate)) {
            throw new IllegalArgumentException("AppTemplate already exists: " + appTemplate.getId());
        }
    }

    /**
     * Gets a template by its id
     * @param id
     * @return
     */
    public AppTemplate getAppTemplate(String id) {
        for (AppTemplate candidate : appTemplates) {
            if (candidate.getId().equals(id)) {
                return candidate;
            }
        }
        return null;
    }

}
