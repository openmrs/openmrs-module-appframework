package org.openmrs.module.appframework.factory;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;

import java.io.IOException;
import java.util.List;

public interface AppFrameworkFactory {

    public List<AppDescriptor> getAppDescriptors() throws IOException;

    public List<Extension> getExtensions() throws IOException;

}
