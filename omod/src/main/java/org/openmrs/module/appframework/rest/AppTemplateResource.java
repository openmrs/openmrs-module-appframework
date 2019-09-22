package org.openmrs.module.appframework.rest;

import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppTemplate;
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

@Resource(name = RestConstants.VERSION_1 + "/apptemplate", supportedClass = AppTemplate.class, supportedOpenmrsVersions = {"1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*"})
public class AppTemplateResource extends BaseDelegatingReadableResource<AppTemplate> {

    private AppFrameworkService getService() {
        return Context.getService(AppFrameworkService.class);
    }

    @Override
    public AppTemplate getByUniqueId(String uid) {
        return getService().getAppTemplate(uid);
    }

    @Override
    public AppTemplate newDelegate() {
        return new AppTemplate();
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("uuid"); // expose "id" as "uuid" for consistency with other REST resources
        description.addProperty("display");
        description.addSelfLink();
        if (representation == Representation.DEFAULT) {
            description.addProperty("description");
            description.addProperty("extensionPoints", Representation.REF);
            description.addProperty("contextModel");
            description.addProperty("configOptions");
        }
        return description;
    }

    @PropertyGetter("uuid")
    public String getUuid(AppTemplate delegate) {
        return delegate.getId();
    }

    @PropertyGetter("display")
    public String getDisplay(AppTemplate delegate) {
        return delegate.getDescription();
    }

    @Override
    public PageableResult doGetAll(RequestContext context) {
        return new NeedsPaging<AppTemplate>(getService().getAllAppTemplates(), context);
    }
}
