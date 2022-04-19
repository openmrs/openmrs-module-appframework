package org.openmrs.module.appframework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;

import javax.validation.Validation;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AllAppDescriptorsTest {

    private AllAppDescriptors allAppDescriptors;

    @BeforeEach
    public void setUp() {
        allAppDescriptors = new AllAppDescriptors(Validation.buildDefaultValidatorFactory().getValidator());
        allAppDescriptors.add(new AppDescriptor("app1", "desc1", "label1", "url1", "iconUrl", "tinyIconUrl", 1));
    }

    @Test
    public void testAddAppFailsWhenAppWithIdExists() {
        assertThrows(IllegalArgumentException.class, () -> allAppDescriptors.add(
                new AppDescriptor("app1", "desc2", "label2", "url2", "iconUrl", "tinyIconUrl", 2)
        ));
    }
}
