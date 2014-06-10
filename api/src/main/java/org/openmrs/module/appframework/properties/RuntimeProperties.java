package org.openmrs.module.appframework.properties;

import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Component to access application runtime properties
 */
@Component("runtimeProperties")
public class RuntimeProperties {

    public Properties getProperties() {
        return OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME);
    }

    public boolean hasProperty(String key) {
        Properties properties = getProperties();

        if (properties == null) {
            return false;
        }

        return properties.contains(key);
    }

    public Object getProperty(String key) {
        Properties properties = getProperties();

        if (getProperties() == null) {
            return null;
        }

        return properties.get(key);
    }

}
