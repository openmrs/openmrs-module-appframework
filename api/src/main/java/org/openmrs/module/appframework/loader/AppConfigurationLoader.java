package org.openmrs.module.appframework.loader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.module.appframework.repository.AllExtensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class AppConfigurationLoader {

    protected Log logger = LogFactory.getLog(getClass());

    private AllAppDescriptors allAppDescriptors;

    private AllExtensions allExtensions;

    @Autowired
    public AppConfigurationLoader(AllAppDescriptors allAppDescriptors, AllExtensions allExtensions) {
        this.allAppDescriptors = allAppDescriptors;
        this.allExtensions = allExtensions;
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    private PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    public void loadConfiguration() throws IOException {
        Resource[] appDefinitionJsonResources = resourceResolver.getResources("classpath*:/apps/*app.json");
        allAppDescriptors.clear();
        for (Resource appDefinitionResource : appDefinitionJsonResources) {
            List<AppDescriptor> appDescriptors;
            try {
                appDescriptors = objectMapper.readValue(appDefinitionResource.getInputStream(), new TypeReference<List<AppDescriptor>>() {});
                allAppDescriptors.add(appDescriptors);
            } catch (IOException e) {
                logger.fatal("Error reading app configuration file", e);
            }
        }

        Resource[] extensionDefinitionJsonResources = resourceResolver.getResources("classpath*:/apps/*extension.json");
        allExtensions.clear();
        for (Resource extensionResource : extensionDefinitionJsonResources) {
            List<Extension> extensions;
            try {
                extensions = objectMapper.readValue(extensionResource.getInputStream(), new TypeReference<List<Extension>>() {});
                allExtensions.add(extensions);
            } catch (IOException e) {
                logger.fatal("Error reading extension configuration file.");
            }
        }
    }

}
