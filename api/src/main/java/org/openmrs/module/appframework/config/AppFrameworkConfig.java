package org.openmrs.module.appframework.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Component used to access AppFrameworkConfigDescriptor
 * See AppFrameworkConfigDescriptor for more information
 */
@Component("appFrameworkConfig")
public class AppFrameworkConfig {

    private final Log log = LogFactory.getLog(getClass());

    public static final String APP_FRAMEWORK_CONFIGURATION_DEFAULT_FILENAME = "appframework-config.json";

    public static final String APP_FRAMEWORK_CONFIGURATION_RUNTIME_PROPERTY = "appframework.config.profiles";

    private List<AppFrameworkConfigDescriptor> descriptors;

    private PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private ObjectMapper objectMapper = new ObjectMapper();

    public AppFrameworkConfig() {
        descriptors = new ArrayList<AppFrameworkConfigDescriptor>();
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        refreshContext();
    }

    public Boolean isEnabled(AppDescriptor app) {

        String id = app.getId();
        Boolean enabled = null;

        // iterate through each descriptor, overriding the previous value, if specified
        for (AppFrameworkConfigDescriptor descriptor : descriptors) {
            enabled = descriptor.getAppConfiguration() != null && descriptor.getAppConfiguration().containsKey(id)
                    ? descriptor.getAppConfiguration().get(id).isEnabled() : enabled;
        }

        return enabled != null ? enabled : getAppsEnabledByDefault();
    }

    public Boolean isEnabled(Extension extension) {
        String id = extension.getId();
        Boolean enabled = null;

        // iterate through each descriptor, overriding the previous value, if specified
        for (AppFrameworkConfigDescriptor descriptor : descriptors) {
            enabled = descriptor.getExtensionConfiguration() != null && descriptor.getExtensionConfiguration().containsKey(id)
                    ? descriptor.getExtensionConfiguration().get(id).isEnabled() : enabled;
        }

        return enabled != null ? enabled : getExtensionsEnabledByDefault();
    }

    public Boolean getLoadAppsFromClasspath() {

        Boolean loadAppFromsClassPath = null;

        // iterate through each descriptor, overriding the previous value, if specified
        for (AppFrameworkConfigDescriptor descriptor : descriptors) {
            loadAppFromsClassPath = descriptor.getLoadAppsFromClasspath() != null
                    ? descriptor.getLoadAppsFromClasspath() : loadAppFromsClassPath;
        }

        return loadAppFromsClassPath != null ? loadAppFromsClassPath : true;  // defaults to true
    }

    public Boolean getAppsEnabledByDefault() {

        Boolean appsEnabledByDefault = null;

        // iterate through each descriptor, overriding the previous value, if specified
        for (AppFrameworkConfigDescriptor descriptor : descriptors) {
            appsEnabledByDefault = descriptor.getAppsEnabledByDefault() != null
                    ? descriptor.getAppsEnabledByDefault() : appsEnabledByDefault;
        }

        return appsEnabledByDefault != null ? appsEnabledByDefault : true; // defaults to true
    }

    public Boolean  getExtensionsEnabledByDefault() {

        Boolean extensionsEnabledByDefault = null;

        // iterate through each descriptor, overriding the previous value, if specified
        for (AppFrameworkConfigDescriptor descriptor : descriptors) {
            extensionsEnabledByDefault = descriptor.getExtensionsEnabledByDefault() != null
                    ? descriptor.getExtensionsEnabledByDefault() : extensionsEnabledByDefault;
        }

        return extensionsEnabledByDefault != null ? extensionsEnabledByDefault : true; // defaults to true

    }

    public void refreshContext() {

        descriptors = new ArrayList<AppFrameworkConfigDescriptor>();

        // the base file is always loaded first, if it exists
        InputStream profileStream = findProfile(APP_FRAMEWORK_CONFIGURATION_DEFAULT_FILENAME);
        if (profileStream != null) {
            try {
                addAppFrameworkConfigDescriptor(objectMapper.readValue(profileStream, AppFrameworkConfigDescriptor.class));
            }
            catch (IOException e) {
                log.error("Unable to load app framework configuration file for profile " + APP_FRAMEWORK_CONFIGURATION_DEFAULT_FILENAME, e);
            }
        }

        // only load other profiles if we have a runtime property configured
        if (Context.getRuntimeProperties().containsKey(APP_FRAMEWORK_CONFIGURATION_RUNTIME_PROPERTY)) {

            for (String profile : Context.getRuntimeProperties().getProperty(APP_FRAMEWORK_CONFIGURATION_RUNTIME_PROPERTY).split(",")) {
                profileStream = findProfile("appframework-config-" + profile.trim() + ".json");
                if (profileStream != null) {
                    try {
                        addAppFrameworkConfigDescriptor(objectMapper.readValue(profileStream, AppFrameworkConfigDescriptor.class));
                    }
                    catch (IOException e) {
                        log.error("Unable to load app framework configuration file for profile " + profile, e);
                    }
                }
            }
        }

    }

    /**
     * For unit testing isEnabled methods
     * @param descriptor
     */
    public void addAppFrameworkConfigDescriptor(AppFrameworkConfigDescriptor descriptor) {
        this.descriptors.add(descriptor);
    }

    private InputStream findProfile(String profileFilename) {

        Exception exception = null;

        // first see if is in the .OpenMRS directory (which will override any file of the same name on the classpath)
        File profileFile = new File(OpenmrsUtil.getApplicationDataDirectory() + File.separatorChar + profileFilename);

        if (profileFile.exists()) {
            try {
                return new FileInputStream(profileFile);
            }
            catch (IOException e){
                exception = e;
            }
        }

        // if not found, check the classpath
        try {
            Resource[] appConfigJsonResource = resourceResolver.getResources("classpath*:/appconfig/" + profileFilename);
            if (appConfigJsonResource != null && appConfigJsonResource.length > 0) {
                if (appConfigJsonResource.length > 1) {
                    log.error("Multiple files named " + profileFilename + " found, using one arbitrarily");
                }
                return appConfigJsonResource[0].getInputStream();
            }
        }
        catch (IOException e) {
            exception  = e;
        }

        if (!profileFilename.equals(APP_FRAMEWORK_CONFIGURATION_DEFAULT_FILENAME)) {
            log.error("Unable to find appframework configuration file " + profileFilename + " either in /appconfig on the classpath or in the OpenMRS application directory", exception);
        }

        return null;
    }
}
