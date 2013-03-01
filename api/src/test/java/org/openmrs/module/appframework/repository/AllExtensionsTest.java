package org.openmrs.module.appframework.repository;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.springframework.test.annotation.ExpectedException;

public class AllExtensionsTest {
    private AllExtensions allExtensions;

    @Before
    public void setUp() {
        allExtensions = new AllExtensions();
        allExtensions.add(new Extension("ext1", "app1", "extensionPoint1", "link", "label", "url"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExtensionFailsWhenExtensionWithIdExists() {
        allExtensions.add(new Extension("ext1", "app2", "extensionPoint2", "link2", "label2", "url2"));
    }
}
