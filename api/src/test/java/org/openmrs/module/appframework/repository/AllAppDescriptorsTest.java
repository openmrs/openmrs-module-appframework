package org.openmrs.module.appframework.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AllAppDescriptorsTest {

    private AllAppDescriptors allAppDescriptors;

    @Before
    public void setUp() {
        allAppDescriptors = new AllAppDescriptors(Validation.buildDefaultValidatorFactory().getValidator());
        allAppDescriptors.add(new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAppFailsWhenAppWithIdExists() {
        allAppDescriptors.add(new AppDescriptor("app1", "desc2", "label2", "url2", "iconUrl", "tinyIconUrl", 2));
    }
}
