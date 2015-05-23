package org.openmrs.module.appframework.rest;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.openmrs.module.appframework.test.Matchers.hasEntry;

public class ExtensionResourceTest extends BaseModuleWebContextSensitiveTest {

    private Extension extension;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("foo", "bar");
        extension = new Extension("id", "appId", "extensionPointId", "type", "label", "url", 1, "priv", params);
    }

    @Test
    public void testDefaultRepresentation() throws Exception {
        SimpleObject actual = new ExtensionResource().asRepresentation(extension, Representation.DEFAULT);
        assertThat(actual, hasEntry("uuid", is("id")));
        assertThat(actual, hasEntry("appId", is("appId")));
        assertThat(actual, hasEntry("extensionPointId", is("extensionPointId")));
        assertThat(actual, hasEntry("type", is("type")));
        assertThat(actual, hasEntry("label", is("label")));
        assertThat(actual, hasEntry("url", is("url")));
        assertThat(actual, hasEntry("order", is(1)));
        assertThat(actual, hasEntry("requiredPrivilege", is("priv")));
        assertThat(actual, hasEntry("extensionParams", hasEntry("foo", "bar")));
    }

}