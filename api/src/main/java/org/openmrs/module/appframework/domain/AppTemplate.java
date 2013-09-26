package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * Descriptor that can be customized and instantiated to create an app.
 * For example a generic registration module might provide a template for a registration app, and expect
 * implementations to instantiate one app per clinical service, each with slightly different configurations.
 */
public class AppTemplate {

    @NotEmpty
    @JsonProperty
    private String id;

    @JsonProperty
    private String description;

    @JsonProperty
    private List<ExtensionPoint> extensionPoints;

    @JsonProperty
    private List<String> contextModel;

    @Valid
    @JsonProperty
    private List<AppTemplateConfigurationOption> configOptions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExtensionPoint> getExtensionPoints() {
        return extensionPoints;
    }

    public void setExtensionPoints(List<ExtensionPoint> extensionPoints) {
        this.extensionPoints = extensionPoints;
    }

    public List<String> getContextModel() {
        return contextModel;
    }

    public void setContextModel(List<String> contextModel) {
        this.contextModel = contextModel;
    }

    public List<AppTemplateConfigurationOption> getConfigOptions() {
        return configOptions;
    }

    public void setConfigOptions(List<AppTemplateConfigurationOption> configOptions) {
        this.configOptions = configOptions;
    }
}
