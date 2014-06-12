package org.openmrs.module.appframework.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Component used to access AppFrameworkConfigDescriptor
 * See AppFrameworkConfigDescriptor for more information
 */
@Component("appFrameworkConfig")
public class AppFrameworkConfig {

    private static final String APP_FRAMEWORK_CONFIGURATION_ENV = "APP_FRAMEWORK_CONFIGURATION";
    public static final String APP_FRAMEWORK_CONFIGURATION_FILE_NAME = "appframework-config.json";

    private File appFrameworkConfigFile;

    private AppFrameworkConfigDescriptor descriptor;

    public AppFrameworkConfig() {

        String appFrameworkConfigFileName = System.getenv(APP_FRAMEWORK_CONFIGURATION_ENV);
        if (appFrameworkConfigFileName == null) {
            appFrameworkConfigFileName = OpenmrsUtil.getApplicationDataDirectory() + File.separatorChar + APP_FRAMEWORK_CONFIGURATION_FILE_NAME;
        }
        appFrameworkConfigFile = new File(appFrameworkConfigFileName);

        refreshContext();
    }

    public Boolean isEnabled(AppDescriptor app) {
        String id = app.getId();
        if (descriptor.getAppsEnabledByDefault() == null || descriptor.getAppsEnabledByDefault()) {
            return  descriptor.getAppConfiguration() == null
                    || !descriptor.getAppConfiguration().containsKey(id)
                    || descriptor.getAppConfiguration().get(id).isEnabled();
        }
        else {
            return (descriptor.getAppConfiguration()!= null && descriptor.getAppConfiguration().containsKey(id))
                    ? descriptor.getAppConfiguration().get(id).isEnabled() : false;
        }

    }

    public Boolean isEnabled(Extension extension) {
        String id = extension.getId();
        if (descriptor.getExtensionsEnabledByDefault() == null || descriptor.getExtensionsEnabledByDefault()) {
            return descriptor.getExtensionConfiguration() == null
                    || !descriptor.getExtensionConfiguration().containsKey(id)
                    || descriptor.getExtensionConfiguration().get(id).isEnabled();
        }
        else {
            return (descriptor.getExtensionConfiguration() != null && descriptor.getExtensionConfiguration().containsKey(id))
                    ? descriptor.getExtensionConfiguration().get(id).isEnabled() : false;
        }
    }

    public void refreshContext() {

        if (this.appFrameworkConfigFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(this.appFrameworkConfigFile);
                descriptor = new ObjectMapper().readValue(inputStream, AppFrameworkConfigDescriptor.class);
            } catch (Exception e) {
                throw new RuntimeException("Unable to load app framework configuration file", e);
            }
        }
        else {
            descriptor = new AppFrameworkConfigDescriptor();
        }

    }

    // the only reason we have this public setter is so we can overwrite in tests with proper file name for component tests
    public void setAppframeworkConfigFile(File appFrameworkConfigFile) {
        this.appFrameworkConfigFile = appFrameworkConfigFile;
    }

    // for unit testing isEnabled methods
    protected void setAppFrameworkConfigDescriptor(AppFrameworkConfigDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}
