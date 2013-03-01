package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class ExtensionPoint {

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
