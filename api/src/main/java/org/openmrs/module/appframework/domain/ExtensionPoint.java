package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

import java.util.List;

public class ExtensionPoint {

    @NotEmpty(message = ValidationErrorMessages.EXTENSION_POINT_ID_NOT_EMPTY_MESSAGE)
    @JsonProperty
    protected String id;

    @JsonProperty
    protected String description;

    @JsonProperty
    protected List<String> supportedExtensionTypes;


    public ExtensionPoint() {
    }

    public ExtensionPoint(String id) {
        this.id = id;
    }

    public ExtensionPoint(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSupportedExtensionTypes() {
        return supportedExtensionTypes;
    }

    public void setSupportedExtensionTypes(List<String> supportedExtensionTypes) {
        this.supportedExtensionTypes = supportedExtensionTypes;
    }
}
