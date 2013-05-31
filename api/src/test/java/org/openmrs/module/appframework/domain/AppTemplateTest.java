package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class AppTemplateTest {

    @Test
    public void testParsing() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppTemplate.json");
        AppTemplate actual = new ObjectMapper().readValue(inputStream, AppTemplate.class);
        assertThat(actual.getId(), is("registrationapp.registerPatient"));
    }

}
