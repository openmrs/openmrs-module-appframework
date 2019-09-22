package org.openmrs.module.appframework.rest;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.module.appframework.service.AdministrativeNotificationService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.resource.api.Listable;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/administrativenotification", supportedClass = AdministrativeNotification.class, supportedOpenmrsVersions = {"1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*"})
public class AdministrativeNotificationResource implements Listable {

    @Override
    public SimpleObject getAll(RequestContext requestContext) throws ResponseException {
        List<AdministrativeNotification> notifications = Context.getService(AdministrativeNotificationService.class).getAdministrativeNotifications();
        return new NeedsPaging<AdministrativeNotification>(notifications, requestContext).toSimpleObject(null);
    }

    @Override
    public String getUri(Object o) {
        // intentionally not implemented (these can't be individually accessed)
        return null;
    }
}
