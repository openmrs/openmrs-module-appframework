package org.openmrs.module.appframework.rest;

import org.openmrs.annotation.Handler;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingConverter;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Handler(supports = ExtensionPoint.class, order = 0)
public class ExtensionPointConverter extends BaseDelegatingConverter<ExtensionPoint> {

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("uuid");
        description.addProperty("display");

        if (representation.equals(Representation.DEFAULT)) {
            description.addProperty("supportedExtensionTypes");
        }

        return description;
    }

    @PropertyGetter("uuid")
    public String getUuid(ExtensionPoint extensionPoint) {
        return extensionPoint.getId();
    }

    @PropertyGetter("display")
    public String getDisplay(ExtensionPoint extensionPoint) {
        return extensionPoint.getDescription();
    }

    @Override
    public ExtensionPoint newInstance(String s) {
        return new ExtensionPoint();
    }

    @Override
    public ExtensionPoint getByUniqueId(String s) {
        throw new IllegalStateException("Cannot fetch an extension point by id");
    }
}
