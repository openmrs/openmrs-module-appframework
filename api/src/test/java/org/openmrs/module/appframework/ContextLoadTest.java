package org.openmrs.module.appframework;

import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;

import static org.junit.Assert.assertNotNull;

public class ContextLoadTest extends BaseModuleContextSensitiveTest {

    @Autowired
    Validator validator;

    @Test
    public void testValidatorIsBeingInstantiated() {
        assertNotNull(validator);
    }

}
