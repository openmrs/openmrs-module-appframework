package org.openmrs.module.appframework.domain;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DomainValidationsTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testValidationOnAppDescriptor() {
        AppDescriptor appDescriptor;
        Set<ConstraintViolation<AppDescriptor>> constraintViolations;

        appDescriptor = new AppDescriptor(null, "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 1);
        constraintViolations = validator.validate(appDescriptor);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.APP_DESCRIPTOR_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());

        appDescriptor = new AppDescriptor("", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 1);
        constraintViolations = validator.validate(appDescriptor);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.APP_DESCRIPTOR_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());

        appDescriptor = new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 1);
        appDescriptor.setExtensionPoints(Arrays.asList(new ExtensionPoint("")));
        constraintViolations = validator.validate(appDescriptor);
        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.EXTENSION_POINT_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());

        appDescriptor = new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 1);
        appDescriptor.setExtensionPoints(Arrays.asList(new ExtensionPoint("extensionPoint1"), new ExtensionPoint("extensionPoint1")));
        constraintViolations = validator.validate(appDescriptor);
        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.APP_DESCRIPTOR_DUPLICATE_EXT_POINT_MESSAGE, constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testValidationOnExtension() {
        Extension extension;
        Set<ConstraintViolation<Extension>> constraintViolations;

        extension = new Extension(null, "app1", "extensionPoint1", "link", "label", "url", 0);
        constraintViolations = validator.validate(extension);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.EXTENSION_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());

        extension = new Extension("", "app1", "extensionPoint1", "link", "label", "url", 0);
        constraintViolations = validator.validate(extension);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.EXTENSION_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testValidationOnExtensionPoint() {
        ExtensionPoint extensionPoint;
        Set<ConstraintViolation<ExtensionPoint>> constraintViolations;

        extensionPoint = new ExtensionPoint(null);
        constraintViolations = validator.validate(extensionPoint);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.EXTENSION_POINT_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());

        extensionPoint = new ExtensionPoint("");
        constraintViolations = validator.validate(extensionPoint);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationErrorMessages.EXTENSION_POINT_ID_NOT_EMPTY_MESSAGE, constraintViolations.iterator().next().getMessage());
    }
}
