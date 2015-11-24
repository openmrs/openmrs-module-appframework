package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * An action that can be chosen in response to an {@link AdministrativeNotification}.
 *
 * Typically a module will implement an AdministrativeNotificationProducer, which will generate one or more
 * {@link AdministrativeNotification}s, each with one or more AdministrativeNotificationActions.
 *
 * The application UI will ensure that choosing this action either executes the specified Javascript, or else navigates
 * to the specified URL. The framework will not change any state to avoid redisplaying the same notification again; the
 * module is responsible for doing this.
 */
public class AdministrativeNotificationAction {

    @NotEmpty
    @JsonProperty
    private String id;

    @JsonProperty
    private String requiredPrivilege;

    @JsonProperty
    private String icon;

    @JsonProperty
    private String label;

    @JsonProperty
    private String url;

    @JsonProperty
    private String script;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequiredPrivilege() {
        return requiredPrivilege;
    }

    public void setRequiredPrivilege(String requiredPrivilege) {
        this.requiredPrivilege = requiredPrivilege;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
