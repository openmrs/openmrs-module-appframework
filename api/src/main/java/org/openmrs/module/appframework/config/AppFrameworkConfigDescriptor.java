package org.openmrs.module.appframework.config;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 * Allows configuration of apps and extensions at runtime via a file "appframework-config.json"
 * This file is not mandatory, it currently allows 1) enabling and disabling apps and extensions, and
 * 2) disabling the default configuration factory (so that no apps are extensions are loaded by default).
 *
 * A primary use case for enabled/disabling apps is customization of different servers using deployment tools like puppet/chef,
 * so you can push server-specific configuration outside of a database (which may be replicated between servers) and the
 * code (which may be shared).  For example, disabling all apps and extensions except reporting-related apps on a
 * reporting server.
 *
 * Accessible via the AppFrameworkConfig component which handles loading the appframework-config.json
 *
 * File format:
 *
 *{
 *  "defaultConfigurationFactoryDisabled":  false,
 *
 *  "appsEnabledByDefault": true,
 *
 *  "appConfiguration": {
 *      "someApp": {
 *          "enabled": false
 *      },
 *      "anotherApp": {
 *          "enabled": true
 *      }
 *  },
 *
 *  "extensionsEnabledByDefault": false,
 *
 *  "extensionConfiguration": {
 *      "someExtension": {
 *          "enabled": true
 *      },
 *      "anotherExtension": {
 *          "enabled": false
 *      }
 *  }
 *}
 */
public class AppFrameworkConfigDescriptor {

    @JsonProperty
    private Boolean loadAppsFromClasspath;

    @JsonProperty
    private Boolean appsEnabledByDefault;

    @JsonProperty
    private Boolean extensionsEnabledByDefault;

    @JsonProperty
    private Map<String,AppConfigDescriptor> appConfiguration;

    @JsonProperty
    private Map<String,ExtensionConfigDescriptor> extensionConfiguration;


    public Boolean getLoadAppsFromClasspath() {
        return loadAppsFromClasspath;
    }

    public void setLoadAppsFromClasspath(Boolean loadAppsFromClasspath) {
        this.loadAppsFromClasspath = loadAppsFromClasspath;
    }

    public Boolean getAppsEnabledByDefault() {
        return appsEnabledByDefault;
    }

    public void setAppsEnabledByDefault(Boolean appsEnabledByDefault) {
        this.appsEnabledByDefault = appsEnabledByDefault;
    }

    public Boolean getExtensionsEnabledByDefault() {
        return extensionsEnabledByDefault;
    }

    public void setExtensionsEnabledByDefault(Boolean extensionsEnabledByDefault) {
        this.extensionsEnabledByDefault = extensionsEnabledByDefault;
    }

    public Map<String, AppConfigDescriptor> getAppConfiguration() {
        return appConfiguration;
    }

    public void setAppConfiguration(Map<String, AppConfigDescriptor> appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public Map<String, ExtensionConfigDescriptor> getExtensionConfiguration() {
        return extensionConfiguration;
    }

    public void setExtensionConfiguration(Map<String, ExtensionConfigDescriptor> extensionConfiguration) {
        this.extensionConfiguration = extensionConfiguration;
    }

}
