package org.openmrs.module.appframework.rest;

import org.junit.jupiter.api.Test;
import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.web.test.jupiter.BaseModuleWebContextSensitiveTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AdministrativeNotificationResourceTest extends BaseModuleWebContextSensitiveTest {

    @Test
    public void testGetAll() {
        AdministrativeNotificationResource resource = new AdministrativeNotificationResource();
        SimpleObject results = resource.getAll(new RequestContext());
        assertThat(((List) results.get("results")).size(), is(1));

        AdministrativeNotification result = (AdministrativeNotification) ((List) results.get("results")).get(0);
        assertThat(result.getId(), is("id"));
        assertThat(result.getLabel(), is("label"));
        assertThat(result.getActions().size(), is(1));
    }
}