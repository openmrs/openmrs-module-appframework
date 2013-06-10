package org.openmrs.module.appframework.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.domain.Extension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppConfigurationLoaderFactory implements AppFrameworkFactory {

    protected Log logger = LogFactory.getLog(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    private PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    public AppConfigurationLoaderFactory() {
    	// Tell the parser to all // and /* style comments.
    	objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }
    
    @Override
    public List<AppTemplate> getAppTemplates() throws IOException {
        Resource[] appDefinitionJsonResources = resourceResolver.getResources("classpath*:/apps/*AppTemplates.json");
        List<AppTemplate> templates = new ArrayList<AppTemplate>();
        for (Resource appDefinitionResource : appDefinitionJsonResources) {
            List<AppTemplate> forResource;
            try {
                forResource = objectMapper.readValue(appDefinitionResource.getInputStream(), new TypeReference<List<AppTemplate>>() {});
                templates.addAll(forResource);
            } catch (IOException e) {
                logger.fatal("Error reading AppTemplates configuration file", e);
            }
        }
        return templates;
    }

    @Override
    public List<AppDescriptor> getAppDescriptors() throws IOException {
        Resource[] appDefinitionJsonResources = resourceResolver.getResources("classpath*:/apps/*app.json");
        List<AppDescriptor> appDescriptors = new ArrayList<AppDescriptor>();
        for (Resource appDefinitionResource : appDefinitionJsonResources) {
            try {
                List<AppDescriptor> appDescriptorsForResource = getAppDescriptorsForResource(appDefinitionResource.getInputStream());
                appDescriptors.addAll(appDescriptorsForResource);
            } catch (IOException e) {
                logger.fatal("Error reading app configuration file: " + appDefinitionResource.getFile().getPath(), e);
            }
        }
        return appDescriptors;
    }

    public List<AppDescriptor> getAppDescriptorsForResource(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, new TypeReference<List<AppDescriptor>>() {});
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
