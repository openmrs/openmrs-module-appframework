package org.openmrs.module.appframework.rest;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.Searchable;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingReadableResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;

import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/extension", supportedClass = Extension.class, supportedOpenmrsVersions = {"1.9.*", "1.10.*", "1.11.*", "1.12.*"})
public class ExtensionResource extends BaseDelegatingReadableResource<Extension> implements Searchable {

    private AppFrameworkService getService() {
        return Context.getService(AppFrameworkService.class);
    }

    @Override
    public Extension getByUniqueId(String uid) {
        for (Extension candidate : getService().getAllEnabledExtensions()) {
            if (candidate.getId().equals(uid)) {
                return candidate;
            }
        }
        return null;
    }

    @Override
    public Extension newDelegate() {
        return new Extension();
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("uuid"); // expose "id" as "uuid" for consistency with other REST resources
        description.addProperty("display");
        description.addSelfLink();
        if (representation == Representation.DEFAULT) {
            description.addProperty("appId");
            description.addProperty("extensionPointId");
            description.addProperty("type");
            description.addProperty("label");
            description.addProperty("url");
            description.addProperty("icon");
            description.addProperty("order");
            description.addProperty("requiredPrivilege");
            description.addProperty("featureToggle");
            description.addProperty("require");
            description.addProperty("script");
            description.addProperty("extensionParams");
            description.addProperty("belongsTo", Representation.REF);
        }
        return description;
    }

    @PropertyGetter("uuid")
    public String getUuid(Extension delegate) {
        return delegate.getId();
    }

    @PropertyGetter("display")
    public String getDisplay(Extension delegate) {
        return delegate.getId();
    }

    /**
     * Supports request parameters (both optional):
     * * "extensionPoint"
     * * "scope" = "user" if not user, then all enabled extensions)
     * @param context
     * @return enabled extensions matching the request parameters
     */
    @Override
    protected PageableResult doSearch(RequestContext context) {
        String extensionPointId = context.getParameter("extensionPoint");
        boolean userOnly = "user".equals(context.getParameter("scope"));
        AppFrameworkService service = getService();
        List<Extension> results;
        if (userOnly) {
            if (StringUtils.isEmpty(extensionPointId)) {
                results = service.getExtensionsForCurrentUser();
            }
            else {
                results = service.getExtensionsForCurrentUser(extensionPointId);
            }
        }
        else {
            if (StringUtils.isEmpty(extensionPointId)) {
                results = service.getAllEnabledExtensions();
            }
            else {
                results = service.getAllEnabledExtensions(extensionPointId);
            }
        }
        return new NeedsPaging<Extension>(results, context);
    }
}
