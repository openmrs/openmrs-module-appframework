package org.openmrs.module.appframework.context;

/**
 * Details of the current user's login session
 */
public class SessionContext {

    protected Integer currentUserId;

    protected Integer currentProviderId;

    protected Integer sessionLocationId;

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getCurrentProviderId() {
        return currentProviderId;
    }

    public void setCurrentProviderId(Integer currentProviderId) {
        this.currentProviderId = currentProviderId;
    }

    public Integer getSessionLocationId() {
        return sessionLocationId;
    }

    public void setSessionLocationId(Integer sessionLocationId) {
        this.sessionLocationId = sessionLocationId;
    }

}
