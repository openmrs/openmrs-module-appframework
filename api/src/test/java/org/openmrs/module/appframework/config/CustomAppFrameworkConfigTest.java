package org.openmrs.module.appframework.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomAppFrameworkConfigTest {

    private CustomAppFrameworkConfig customAppFrameworkConfig;
    
    @Before
    public void setup() {
        customAppFrameworkConfig = new CustomAppFrameworkConfig();
    }
    
    @Test
    public void enabledByDefault_shouldDetermineAppEnabledIfEnabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("anotherApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(true));
    }

    @Test
    public void enabledByDefault_shouldDetermineAppDisabledIfDisabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("someApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(false));
    }

    @Test
    public void enabledByDefault_shouldDetermineAppEnabledEvenIfNotEnabledDirectly() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("randomApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(true));
    }

    @Test
    public void disabledByDefault_shouldDetermineAppEnabledIfEnabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration2.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("anotherApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(true));
    }

    @Test
    public void disabledByDefault_shouldDetermineAppDisabledIfDisabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration2.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("someApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(false));
    }

    @Test
    public void disabledByDefault_shouldDetermineAppDisabledIfNotEnabledDirectly() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration2.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("randomApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(false));
    }

    @Test
    public void noDefault_shouldDetermineAppEnabledIfEnabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration3.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("anotherApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(true));
    }

    @Test
    public void noDefault_shouldDetermineAppDisabledIfDisabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration3.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("someApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(false));
    }

    @Test
    public void noByDefault_shouldDetermineAppEnabledEvenIfNotEnabledDirectly() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration3.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        AppDescriptor app = new AppDescriptor("randomApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(true));
    }

    @Test
    public void enabledByDefault_shouldDetermineExtensionEnabledIfEnabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration2.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("someExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(true));
    }

    @Test
    public void enabledByDefault_shouldDetermineExtensionDisabledIfDisabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration2.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("anotherExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(false));
    }

    @Test
    public void enabledByDefault_shouldDetermineExtensionEnabledEvenIfNotEnabledDirectly() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration2.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("randomExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(true));
    }

    @Test
    public void disabledByDefault_shouldDetermineExtensionEnabledIfEnabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("someExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(true));
    }

    @Test
    public void disabledByDefault_shouldDetermineExtensionDisabledIfDisabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("anotherExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(false));
    }

    @Test
    public void disabledByDefault_shouldDetermineExtensionDisabledIfNotEnabledDirectly() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration1.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("randomExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(false));
    }

    @Test
    public void noDefault_shouldDetermineExtensionEnabledIfEnabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration3.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("someExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(true));
    }

    @Test
    public void noDefault_shouldDetermineExtensionDisabledIfDisabledViaConfiguration() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration3.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("anotherExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(false));
    }

    @Test
    public void noByDefault_shouldDetermineExtensionEnabledEvenIfNotEnabledDirectly() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testAppframeworkConfiguration3.json");
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new ObjectMapper().readValue(inputStream, CustomAppFrameworkConfigDescriptor.class));
        Extension extension = new Extension("randomExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(true));
    }

    @Test
    public void noAppFrameworkConfig_shouldDetermineAppEnabled() throws Exception {
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new CustomAppFrameworkConfigDescriptor());
        AppDescriptor app = new AppDescriptor("anyApp", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(app), is(true));
    }

    @Test
    public void noAppFrameworkConfig_shouldDetermineExtensionEnabled() throws Exception {
        customAppFrameworkConfig.setAppFrameworkConfigDescriptor(new CustomAppFrameworkConfigDescriptor());
        Extension extension = new Extension("anyExtension", null, null, null, null, null, 0);
        assertThat(customAppFrameworkConfig.isEnabled(extension), is(true));
    }


}
