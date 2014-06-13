package org.openmrs.module.appframework.config;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 * Allows configuration of apps and extensions at runtime via a file "appframework-config.json"
 * This file is not mandatory, and currently allows only enabling and disabling apps and extensions.
 * A primary use case is customization of different servers using deployment tools like puppet/chef,
 * so you can push server-specific configuration outside of a database (which may be replicated between servers) and the
 * code (which may be shared).  For example, disabling all apps and extensions except reporting-related apps on a
 * reporting server.
 *
 * Accessible via the CustomAppFrameworkConfig component which handles loading the custom-appframework-config.json
 *
 * File format:
 *
 *{
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
public class CustomAppFrameworkConfigDescriptor {

    @JsonProperty
    private Boolean appsEnabledByDefault;

    @JsonProperty
    private Boolean extensionsEnabledByDefault;

    @JsonProperty
    private Map<String,CustomAppConfigDescriptor> appConfiguration;

    @JsonProperty
    private Map<String,CustomExtensionConfigDescriptor> extensionConfiguration;


    public Boolean getAppsEnabledByDefault() {
        return appsEnabledByDefault;
    }

    public Boolean getExtensionsEnabledByDefault() {
        return extensionsEnabledByDefault;
    }


    public Map<String, CustomAppConfigDescriptor> getAppConfiguration() {
        return appConfiguration;
    }

    public void setAppConfiguration(Map<String, CustomAppConfigDescriptor> appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public Map<String, CustomExtensionConfigDescriptor> getExtensionConfiguration() {
        return extensionConfiguration;
    }

    public void setExtensionConfiguration(Map<String, CustomExtensionConfigDescriptor> extensionConfiguration) {
        this.extensionConfiguration = extensionConfiguration;
    }

}
