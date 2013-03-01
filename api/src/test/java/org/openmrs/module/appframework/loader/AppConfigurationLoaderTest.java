package org.openmrs.module.appframework.loader;

import org.junit.Test;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AppConfigurationLoaderTest extends BaseModuleContextSensitiveTest {

    @Autowired
    AppConfigurationLoader appConfigurationLoader;

    @Autowired
    AllAppDescriptors allAppDescriptors;

    @Autowired
    AllExtensions allExtensions;

    @Test
    public void testConfigurationLoad() throws IOException {
        appConfigurationLoader.loadConfiguration();

        assertEquals(3, allAppDescriptors.getAppDescriptors().size());
        assertEquals(2, allExtensions.getExtensions().size());
    }

}
