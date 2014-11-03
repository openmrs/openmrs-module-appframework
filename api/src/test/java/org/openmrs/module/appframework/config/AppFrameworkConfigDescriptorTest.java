package org.openmrs.module.appframework.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AppFrameworkConfigDescriptorTest {

    @Test
    public void testParsing() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        AppFrameworkConfigDescriptor appFrameworkConfigDescriptor = new ObjectMapper().readValue(inputStream, AppFrameworkConfigDescriptor.class);
        assertThat(appFrameworkConfigDescriptor.getAppsEnabledByDefault(), is(true));
        assertThat(appFrameworkConfigDescriptor.getExtensionsEnabledByDefault(), is(false));
        assertThat(appFrameworkConfigDescriptor.getLoadAppsFromClasspath(), is(true));
        assertThat(appFrameworkConfigDescriptor.getAppConfiguration().get("someApp").getEnabled(), is(false));
        assertThat(appFrameworkConfigDescriptor.getAppConfiguration().get("anotherApp").getEnabled(), is(true));
        assertThat(appFrameworkConfigDescriptor.getExtensionConfiguration().get("someExtension").getEnabled(), is(true));
        assertThat(appFrameworkConfigDescriptor.getExtensionConfiguration().get("anotherExtension").getEnabled(), is(false));
    }

}
