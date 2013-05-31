package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class AppDescriptorTest {

    @Test
    public void testParsing() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testApp.json");
        AppDescriptor actual = new ObjectMapper().readValue(inputStream, AppDescriptor.class);
        assertThat(actual.getId(), is("referenceapplication.registerPatient.outpatient"));
    }

}
