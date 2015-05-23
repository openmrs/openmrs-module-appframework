package org.openmrs.module.appframework.rest;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.TextNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.AppFrameworkActivator;
import org.openmrs.module.appframework.domain.AppTemplate;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.openmrs.module.appframework.test.Matchers.hasEntry;

public class AppTemplateResourceTest extends BaseModuleWebContextSensitiveTest {

    @Autowired
    private AppFrameworkService service;

    @Before
    public void setUp() throws Exception {
        new AppFrameworkActivator().contextRefreshed();
    }

    @Test
    public void testDefaultRepresentation() throws Exception {
        AppTemplate appTemplate = service.getAppTemplate("testing.registrationapp.registerPatient");
        SimpleObject actual = new AppTemplateResource().asRepresentation(appTemplate, Representation.DEFAULT);

        assertThat(actual, hasEntry("uuid", "testing.registrationapp.registerPatient"));
        assertThat(actual, hasEntry("contextModel", containsInAnyOrder("createdPatientId")));
        assertThat(actual, hasEntry("configOptions", containsInAnyOrder(
                allOf(
                        hasProperty("name", is("extraFields")),
                        hasProperty("description", is("blah blah how to set it up")),
                        hasProperty("defaultValue", isEmptyJsonArray())),
                allOf(hasProperty("name", is("urlOnSuccess")),
                        hasProperty("defaultValue", textNodeLike("patientDashboard.page?patientId={{appContext.createdPatientId}}")))
        )));
    }

    private Matcher<Object> textNodeLike(final String expected) {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof TextNode && OpenmrsUtil.nullSafeEquals(((TextNode) o).getTextValue(), expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("TextNode with value " + expected);
            }
        };
    }

    private Matcher<?> isEmptyJsonArray() {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof ArrayNode && ((ArrayNode) o).size() == 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("empty jackson ArrayNode");
            }
        };
    }

}