package org.openmrs.module.appframework;

import org.openmrs.module.appframework.config.AppFrameworkConfig;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.factory.AppConfigurationLoaderFactory;
import org.openmrs.module.appframework.factory.AppFrameworkFactory;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllAppTemplates;
import org.openmrs.module.appframework.repository.AllFreeStandingExtensions;

import java.util.Arrays;
import java.util.Collections;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class AppTestUtil {

    /**
     * For mock-based tests.
     * Parses AppTemplates, Apps, and Extensions, and returns a specific app.
     * @param id
     * @return
     */
    public static AppDescriptor getAppDescriptor(String id) {
        Validator validator = mock(Validator.class);
        when(validator.validate(anyObject())).thenReturn(Collections.<ConstraintViolation<Object>>emptySet());
        AllAppDescriptors allAppDescriptors = new AllAppDescriptors(validator);
        AppFrameworkConfig config = mock(AppFrameworkConfig.class);
        new AppFrameworkActivator().registerAppsAndExtensions(Arrays.<AppFrameworkFactory>asList(new AppConfigurationLoaderFactory()),
                new AllAppTemplates(validator), allAppDescriptors, new AllFreeStandingExtensions(validator), config);
        return allAppDescriptors.getAppDescriptor(id);
    }

    /**
     * For mock-based tests.
     * Parses AppTemplates, Apps, and Extensions, and returns a specific template.
     * @param id
     * @return
     */
    public static AppTemplate getAppTemplate(String id) {
        Validator validator = mock(Validator.class);
        when(validator.validate(anyObject())).thenReturn(Collections.<ConstraintViolation<Object>>emptySet());
        AllAppTemplates allAppTemplates = new AllAppTemplates(validator);
        AppFrameworkConfig config = mock(AppFrameworkConfig.class);
        new AppFrameworkActivator().registerAppsAndExtensions(Arrays.<AppFrameworkFactory>asList(new AppConfigurationLoaderFactory()),
                allAppTemplates, new AllAppDescriptors(validator), new AllFreeStandingExtensions(validator), config);
        return allAppTemplates.getAppTemplate(id);
    }

}
