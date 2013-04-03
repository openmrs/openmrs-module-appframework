package org.openmrs.module.appframework.domain;

import org.openmrs.BaseOpenmrsObject;

public class ComponentState extends BaseOpenmrsObject {

    private Integer componentStateId;

    private String componentId;

    private ComponentType componentType;

    private Boolean enabled;

    public ComponentState() { }

    public ComponentState(String componentId, ComponentType componentType, boolean enabled) {
        this.componentId = componentId;
        this.componentType = componentType;
        this.enabled = enabled;
    }

    public Integer getComponentStateId() {
        return componentStateId;
    }

    public void setComponentStateId(Integer componentStateId) {
        this.componentStateId = componentStateId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    @Override
    public Integer getId() {
        return componentStateId;
    }

    @Override
    public void setId(Integer id) {
        setComponentStateId(id);
    }
}
