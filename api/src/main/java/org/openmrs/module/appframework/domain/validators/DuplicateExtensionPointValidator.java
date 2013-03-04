package org.openmrs.module.appframework.domain.validators;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.ExtensionPoint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DuplicateExtensionPointValidator implements ConstraintValidator<NoDuplicateExtensionPoint, AppDescriptor> {
    @Override
    public void initialize(NoDuplicateExtensionPoint constraintAnnotation) {
    }

    @Override
    public boolean isValid(AppDescriptor value, ConstraintValidatorContext context) {
        if (null == value || null == value.getExtensionPoints()) return true;

        for(ExtensionPoint extensionPointToMatch : value.getExtensionPoints()) {
            for (ExtensionPoint extensionPoint : value.getExtensionPoints()) {
                if (extensionPointToMatch != extensionPoint &&
                    extensionPointToMatch.getId().equalsIgnoreCase(extensionPoint.getId()))
                    return false;
            }
        }

        return true;
    }
}
