package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Lets an AppTemplate describe how it can be configured
 */
public class AppTemplateConfigurationOption {

    @NotEmpty
    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private JsonNode defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonNode getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(JsonNode defaultValue) {
        this.defaultValue = defaultValue;
    }
}
