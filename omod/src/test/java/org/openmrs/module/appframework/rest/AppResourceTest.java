package org.openmrs.module.appframework.rest;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.ExtensionPoint;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;

import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.openmrs.module.appframework.test.Matchers.hasEntry;

public class AppResourceTest extends BaseModuleWebContextSensitiveTest {

    private AppDescriptor app;

    @Before
    public void setUp() throws Exception {
        ExtensionPoint extensionPoint1 = new ExtensionPoint("extPtId", "extDescr");
        ExtensionPoint extensionPoint2 = new ExtensionPoint("two", "2d");
        app = new AppDescriptor("id", "description", "label", "http://url", "icon-something", "icon-tiny", 5,
                "privilege", asList(extensionPoint1, extensionPoint2));
    }

    @Test
    public void testDefaultRepresentation() throws Exception {
        SimpleObject actual = new AppResource().asRepresentation(app, Representation.DEFAULT);
        assertThat(actual, hasEntry("uuid", is("id")));
        assertThat(actual, hasEntry("description", is("description")));
        assertThat(actual, hasEntry("label", is("label")));
        assertThat(actual, hasEntry("url", is("http://url")));
        assertThat(actual, hasEntry("icon", is("icon-something")));
        assertThat(actual, hasEntry("tinyIcon", is("icon-tiny")));
        assertThat(actual, hasEntry("order", is(5)));
        assertThat(actual, hasEntry("requiredPrivilege", is("privilege")));
        assertThat(actual, hasEntry("extensionPoints", containsInAnyOrder(allOf(
                        hasEntry("uuid", "extPtId"),
                        hasEntry("display", "extDescr")
                ),
                allOf(
                        hasEntry("uuid", "two"),
                        hasEntry("display", "2d")
                ))));
    }

}