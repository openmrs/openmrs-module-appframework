package org.openmrs.module.appframework.factory;

import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;

import java.io.IOException;
import java.util.List;

public interface AppFrameworkFactory {

    List<AppDescriptor> getAppDescriptors() throws IOException;

    List<Extension> getExtensions() throws IOException;

    List<AppTemplate> getAppTemplates() throws IOException;

}
