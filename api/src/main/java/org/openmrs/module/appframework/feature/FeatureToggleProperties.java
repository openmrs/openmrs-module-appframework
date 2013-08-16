/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.appframework.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@Component("featureToggles")
public class FeatureToggleProperties {

    private static final String FEATURE_TOGGLE_PROPERTIES_ENV = "FEATURE_TOGGLE_PROPERTIES";
    public static final String FEATURE_TOGGLE_PROPERTIES_FILE_NAME = "feature_toggles.properties";

    private Log log = LogFactory.getLog(getClass());
    private File propertiesFile;

    FeatureToggleProperties() {
        String propertiesFileName = System.getenv(FEATURE_TOGGLE_PROPERTIES_ENV);
        if (propertiesFileName == null) {
            propertiesFileName = OpenmrsUtil.getApplicationDataDirectory() + File.separatorChar + FEATURE_TOGGLE_PROPERTIES_FILE_NAME;
        }

        propertiesFile = new File(propertiesFileName);
    }

    // TODO: find a better way to this--this public setter is just used to override the file in test scripts
    public void setPropertiesFile(File propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public boolean isFeatureEnabled(String key) {
        Properties toggles = loadToggles();
        return Boolean.parseBoolean(toggles.getProperty(key, "false"));
    }

    public Map<Object,Object> getToggleMap() {
        Properties toggles = loadToggles();
        return Collections.unmodifiableMap(toggles);
    }

    private Properties loadToggles() {
        Properties toggles = new Properties();

        if(propertiesFile.exists()){
            try {
                FileInputStream inputStream = new FileInputStream(propertiesFile);
                toggles.load(inputStream);
                inputStream.close();
            } catch (IOException e) {
                log.error("Problem loading feature_toggles.properties file. Error: ", e);
            }
        }

        return toggles;
    }
}
