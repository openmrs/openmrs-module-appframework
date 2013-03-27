package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

public class ExtensionPoint {

    @NotEmpty(message = ValidationErrorMessages.EXTENSION_POINT_ID_NOT_EMPTY_MESSAGE)
    @JsonProperty
    protected String id;

    @JsonProperty
    protected String description;


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
}
