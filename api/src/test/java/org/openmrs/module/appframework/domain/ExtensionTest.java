package org.openmrs.module.appframework.domain;

import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.module.appframework.template.TemplateFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ExtensionTest {

    private Extension newExtension() {
        return new Extension() {
            @Override
            TemplateFactory getTemplateFactory() {
                return new TemplateFactory();
            }
        };
    }

    @Test
    public void testUrlSubstitution() throws Exception {
        Map<String, Object> contextModel = new HashMap<String, Object>();
        contextModel.put("patient", new Patient(7));
        contextModel.put("foo", "bar");

        Extension extension = newExtension();
        extension.setUrl("url?ptId={{patient.patientId}}&q={{foo}}");

        assertThat(extension.url("/openmrs", contextModel), is("/openmrs/url?ptId=7&q=bar"));
    }

    @Test
    public void testUrlForScript() throws Exception {
        Extension extension = newExtension();
        extension.setType("script");
        extension.setScript("window.alert('Hello, world.')");

        assertThat(extension.url("/openmrs", Collections.<String, Object>emptyMap()), is("javascript:window.alert('Hello, world.')"));
    }

}
