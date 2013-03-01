package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

public class Extension {

    @JsonProperty
    protected String id;

    @JsonProperty
    protected String appId;

    @JsonProperty
    protected String extensionPointId;

    @JsonProperty
    protected String type;

    @JsonProperty
    protected String label;

    @JsonProperty
    protected String url;

    @JsonProperty
    protected Map<String, Object> extensionParams;


    public Extension() {
    }

    public Extension(String id, String appId, String extensionPointId, String type, String label, String url) {
        this.id = id;
        this.appId = appId;
        this.extensionPointId = extensionPointId;
        this.type = type;
        this.label = label;
        this.url = url;
    }

    public Extension(String id, String appId, String extensionPointId, String type, String label, String url, Map<String, Object> extensionParams) {
        this.id = id;
        this.appId = appId;
        this.extensionPointId = extensionPointId;
        this.type = type;
        this.label = label;
        this.url = url;
        this.extensionParams = extensionParams;
    }


    public String getId() {
        return id;
    }

    public String getAppId() {
        return appId;
    }

    public String getExtensionPointId() {
        return extensionPointId;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getExtensionParams() {
        return extensionParams;
    }

    public void setExtensionParams(Map<String, Object> extensionParams) {
        this.extensionParams = extensionParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Extension)) return false;

        Extension extension = (Extension) o;

        if (!id.equals(extension.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
