package org.openmrs.module.appframework.repository;

import org.openmrs.module.appframework.domain.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class AllExtensions {

    private List<Extension> extensions = new ArrayList<Extension>();

    private Validator validator;

    @Autowired
    public AllExtensions(Validator validator) {
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
        }
    }

    private void validate(Extension extension) {
        Set<ConstraintViolation<Extension>> constraintViolations = validator.validate(extension);
        if (!constraintViolations.isEmpty()) throw new IllegalArgumentException(constraintViolations.iterator().next().getMessage());

        if (this.extensions.contains(extension)) throw new IllegalArgumentException("Extension already exists");
    }

    public List<Extension> getExtensions() {
        return extensions;
    }

    public void clear() {
        extensions.clear();
    }
}
