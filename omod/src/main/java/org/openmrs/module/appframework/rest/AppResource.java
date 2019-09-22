package org.openmrs.module.appframework.rest;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingReadableResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;

@Resource(name = RestConstants.VERSION_1 + "/app", supportedClass = AppDescriptor.class, supportedOpenmrsVersions = {"1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*"})
public class AppResource extends BaseDelegatingReadableResource<AppDescriptor> {

    private AppFrameworkService getService() {
        return Context.getService(AppFrameworkService.class);
    }

    @Override
    public AppDescriptor getByUniqueId(String uid) {
        return getService().getApp(uid);
    }

    @Override
    public AppDescriptor newDelegate() {
        return new AppDescriptor();
    }

    /**
     * We only return _enabled_ apps, since the client really shouldn't know about disabled ones
     * @param context
     * @return
     */
    @Override
    public PageableResult doGetAll(RequestContext context) {
        return new NeedsPaging<AppDescriptor>(getService().getAllEnabledApps(), context);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("uuid"); // expose "id" as "uuid" for consistency with other REST resources
        description.addProperty("display");
        description.addSelfLink();
        if (representation == Representation.DEFAULT) {
            description.addProperty("description");
            description.addProperty("template", Representation.REF);
            description.addProperty("label");
            description.addProperty("url");
            description.addProperty("icon");
            description.addProperty("tinyIcon");
            description.addProperty("order");
            description.addProperty("requiredPrivilege");
            description.addProperty("featureToggle");
            description.addProperty("config");
            description.addProperty("extensionPoints", Representation.REF);
            description.addProperty("extensions", Representation.REF);
            description.addProperty("contextModel");
        }
        return description;
    }

    @PropertyGetter("uuid")
    public String getUuid(AppDescriptor delegate) {
        return delegate.getId();
    }

    @PropertyGetter("display")
    public String getDisplay(AppDescriptor delegate) {
        return delegate.getDescription();
    }
}
