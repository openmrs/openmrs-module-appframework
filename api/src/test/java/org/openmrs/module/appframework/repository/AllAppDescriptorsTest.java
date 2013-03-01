package org.openmrs.module.appframework.repository;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;

public class AllAppDescriptorsTest {

    private AllAppDescriptors allAppDescriptors;

    @Before
    public void setUp() {
        allAppDescriptors = new AllAppDescriptors();
        allAppDescriptors.add(new AppDescriptor("app1", "desc1", "label1", "url1", "iconurl", "tinyIconurl", 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAppFailsWhenAppWithIdExists() {
        allAppDescriptors.add(new AppDescriptor("app1", "desc2", "label2", "url2", "iconurl", "tinyIconurl", 2));
    }
}
