package org.openmrs.module.appframework.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomAppFrameworkConfigDescriptorTest {

    @Test
    public void testParsing() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        CustomAppFrameworkConfigDescriptor customAppFrameworkConfigDescriptor = new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class);
        assertThat(customAppFrameworkConfigDescriptor.getAppsEnabledByDefault(), is(true));
        assertThat(customAppFrameworkConfigDescriptor.getExtensionsEnabledByDefault(), is(false));
        assertThat(customAppFrameworkConfigDescriptor.getAppConfiguration().get("someApp").getEnabled(), is(false));
        assertThat(customAppFrameworkConfigDescriptor.getAppConfiguration().get("anotherApp").getEnabled(), is(true));
        assertThat(customAppFrameworkConfigDescriptor.getExtensionConfiguration().get("someExtension").getEnabled(), is(true));
        assertThat(customAppFrameworkConfigDescriptor.getExtensionConfiguration().get("anotherExtension").getEnabled(), is(false));
    }

}
