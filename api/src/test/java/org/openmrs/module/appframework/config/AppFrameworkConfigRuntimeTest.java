package org.openmrs.module.appframework.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AppFrameworkConfigRuntimeTest extends BaseModuleContextSensitiveTest {

    @Before
    public void setup() {
        runtimeProperties.setProperty(AppFrameworkConfig.APP_FRAMEWORK_CONFIGURATION_RUNTIME_PROPERTY, "base  ,hospital");
        new AppFrameworkActivator().contextRefreshed();
    }

    @Test
    public void shouldLoadConfigsBasedOnRuntimeProperties() {
       AppFrameworkConfig appFrameworkConfig = Context.getRegisteredComponent("appFrameworkConfig", AppFrameworkConfig.class);

       AppDescriptor app = new AppDescriptor("someApp", null, null, null, null, null, 0);
       assertThat(appFrameworkConfig.isEnabled(app), is(true));
       app = new AppDescriptor("anotherApp", null, null, null, null, null, 0);
       assertThat(appFrameworkConfig.isEnabled(app), is(true));

        Extension extension = new Extension("someExtension", null, null, null, null, null, 0);
        assertThat(appFrameworkConfig.isEnabled(extension), is(false));
        extension = new Extension("anotherExtension", null, null, null, null, null, 0);
        assertThat(appFrameworkConfig.isEnabled(extension), is(false));
    }

    @After
    public void teardown() {
        runtimeProperties.remove(AppFrameworkConfig.APP_FRAMEWORK_CONFIGURATION_RUNTIME_PROPERTY);
        new AppFrameworkActivator().contextRefreshed();
    }
}
