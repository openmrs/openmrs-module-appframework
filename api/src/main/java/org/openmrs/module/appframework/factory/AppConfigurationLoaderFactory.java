package org.openmrs.module.appframework.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppConfigurationLoaderFactory implements AppFrameworkFactory {

    protected Log logger = LogFactory.getLog(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    private PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @Override
    public List<AppDescriptor> getAppDescriptors() throws IOException {
        Resource[] appDefinitionJsonResources = resourceResolver.getResources("classpath*:/apps/*app.json");
        List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();
        for (Resource appDefinitionResource : appDefinitionJsonResources) {
            List<AppDescriptor> appDescriptorsForResource;
            try {
                appDescriptorsForResource = objectMapper.readValue(appDefinitionResource.getInputStream(), new TypeReference<List<AppDescriptor>>() {});
                appDescriptors.addAll(appDescriptorsForResource);
            } catch (IOException e) {
                logger.fatal("Error reading app configuration file", e);
            }
        }
        return appDescriptors;
    }

    @Override
    public List<Extension> getExtensions() throws IOException {
        Resource[] extensionDefinitionJsonResources = resourceResolver.getResources("classpath*:/apps/*extension.json");
        List<Extension> extensions = new ArrayList<Extension>();
        for (Resource extensionResource : extensionDefinitionJsonResources) {
            List<Extension> extensionsForResource;
            try {
                extensionsForResource = objectMapper.readValue(extensionResource.getInputStream(), new TypeReference<List<Extension>>() {});
                extensions.addAll(extensionsForResource);
            } catch (IOException e) {
                logger.fatal("Error reading extension configuration file: " + extensionResource.getFilename(), e);
            }
        }
        return extensions;
    }
}
