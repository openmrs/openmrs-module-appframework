package org.openmrs.module.appframework.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * A notification message, and its associated actions, that can be shown to a user prominently in administrative parts
 * of the UI, including on the home screen. These messages are not intended for patient-level alerts, and are not
 * displayed on patient screens.
 *
 * For example the Atlas module might show "Please add yourself to the OpenMRS Atlas" with links to "Configure"
 * and "Dismiss".
 *
 * A module that wants to generate these notifications should register a bean that implements the
 * AdministrativeNotificationProducer interface.
 */
public class AdministrativeNotification {

    @NotEmpty
    @JsonProperty
    private String id;

    @JsonProperty
    private String requiredPrivilege;

    @JsonProperty
    private String icon;

    @JsonProperty
    private String cssClass;

    @JsonProperty
    private String label;

    @JsonProperty
    private List<AdministrativeNotificationAction> actions;

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

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AdministrativeNotificationAction> getActions() {
        return actions;
    }

    public void setActions(List<AdministrativeNotificationAction> actions) {
        this.actions = actions;
    }

}
