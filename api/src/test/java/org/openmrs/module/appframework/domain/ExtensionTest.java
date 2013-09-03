package org.openmrs.module.appframework.domain;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ExtensionTest {

    @Test
    public void testUrlSubstitution() throws Exception {
        Map<String, Object> contextModel = new HashMap<String, Object>();
        contextModel.put("patient.id", 7);
        contextModel.put("foo", "bar");

        Extension extension = new Extension();
        extension.setUrl("url?ptId={{patient.id}}&q={{foo}}");

        assertThat(extension.url("openmrs", contextModel), is("/openmrs/url?ptId=7&q=bar"));
    }

    @Test
    public void testUrlForScript() throws Exception {
        Extension extension = new Extension();
        extension.setType("script");
        extension.setScript("window.alert('Hello, world.')");

        assertThat(extension.url("openmrs", Collections.<String, Object>emptyMap()), is("javascript:window.alert('Hello, world.')"));
    }

}
