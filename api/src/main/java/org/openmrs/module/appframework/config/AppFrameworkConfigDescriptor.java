package org.openmrs.module.appframework.config;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 * Allows configuration of apps and extensions at runtime via a file "appframework-config.json"
 * This file is not mandatory, and currently allows only enabling and disabling apps and extensions.
 * Example use case would be disabling all apps and extensions except reporting-related apps on a
 * reporting server.
 *
 * Accessible via the AppFrameworkConfig component which handles loading the appframework-config.json
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
public class AppFrameworkConfigDescriptor {

    @JsonProperty
    private Boolean appsEnabledByDefault;

    @JsonProperty
    private Boolean extensionsEnabledByDefault;

    @JsonProperty
    private Map<String,AppConfigDescriptor> appConfiguration;

    @JsonProperty
    private Map<String,ExtensionConfigDescriptor> extensionConfiguration;


    public Boolean getAppsEnabledByDefault() {
        return appsEnabledByDefault;
    }

    public Boolean getExtensionsEnabledByDefault() {
        return extensionsEnabledByDefault;
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
