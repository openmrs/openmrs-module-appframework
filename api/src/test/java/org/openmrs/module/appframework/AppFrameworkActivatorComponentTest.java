package org.openmrs.module.appframework;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.config.AppFrameworkConfig;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.factory.AppFrameworkFactory;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllAppTemplates;
import org.openmrs.module.appframework.repository.AllFreeStandingExtensions;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AppFrameworkActivatorComponentTest extends BaseModuleContextSensitiveTest {

    @Test
    public void testRegisterAppsAndExtensions() throws Exception {
        AppFrameworkActivator activator = new AppFrameworkActivator();

        AllAppTemplates allAppTemplates = Context.getRegisteredComponents(AllAppTemplates.class).get(0);
        AllAppDescriptors allAppDescriptors = Context.getRegisteredComponents(AllAppDescriptors.class).get(0);
        AllFreeStandingExtensions allFreeStandingExtensions = Context.getRegisteredComponents(AllFreeStandingExtensions.class).get(0);
        AppFrameworkConfig config = Context.getRegisteredComponent("appFrameworkConfig", AppFrameworkConfig.class);

        List<AppFrameworkFactory> factories = new ArrayList<AppFrameworkFactory>();
        factories.add(new Factory("one"));
        factories.add(new Factory("two"));

        activator.registerAppsAndExtensions(factories, allAppTemplates, allAppDescriptors, allFreeStandingExtensions, config);

        assertThat(allAppDescriptors.getAppDescriptors().size(), is(2));
        assertThat(allAppDescriptors.getAppDescriptors().get(0).getId(), is("one"));
        assertThat(allAppDescriptors.getAppDescriptors().get(1).getId(), is("two"));

        assertThat(allFreeStandingExtensions.getExtensions().size(), is(2));
        assertThat(allFreeStandingExtensions.getExtensions().get(0).getId(), is("one"));
        assertThat(allFreeStandingExtensions.getExtensions().get(1).getId(), is("two"));
    }

    private class Factory implements AppFrameworkFactory {

        private String id;

        public Factory(String id) {
            this.id = id;
        }

        @Override
        public List<AppDescriptor> getAppDescriptors() throws IOException {
            return Arrays.asList(new AppDescriptor(this.id, "description", "label", "url", "icon", "tinyIcon", 1));
        }

        @Override
        public List<Extension> getExtensions() throws IOException {
            return Arrays.asList(new Extension(this.id, null, "extensionPointId", "link", "label", "url", 1));
        }

        @Override
        public List<AppTemplate> getAppTemplates() throws IOException {
            return null;
        }
    }

}
