package org.openmrs.module.appframework.rest;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AdministrativeNotificationResourceTest extends BaseModuleWebContextSensitiveTest {

    @Autowired
    private AnnotationMethodHandlerAdapter handlerAdapter;

    @Autowired
    private List<DefaultAnnotationHandlerMapping> handlerMappings;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testGetAll() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(RequestMethod.GET.toString(),
                "/rest/v1/administrativenotification");
        request.addHeader("content-type", "application/json");
        SimpleObject results = deserialize(handle(request));

        assertThat(((List) results.get("results")).size(), is(1));

        Map<String, Object> result = (Map<String, Object>) ((List) results.get("results")).get(0);
        assertThat((String) result.get("id"), is("id"));
        assertThat((String) result.get("label"), is("label"));
        assertThat(((Collection) result.get("actions")).size(), is(1));
    }

    /**
     * Passes the given request to a proper controller.
     *
     * @param request
     * @return
     * @throws Exception
     */
    public MockHttpServletResponse handle(HttpServletRequest request) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecutionChain handlerExecutionChain = null;
        for (DefaultAnnotationHandlerMapping handlerMapping : handlerMappings) {
            handlerExecutionChain = handlerMapping.getHandler(request);
            if (handlerExecutionChain != null) {
                break;
            }
        }
        Assert.assertNotNull("The request URI does not exist", handlerExecutionChain);

        handlerAdapter.handle(request, response, handlerExecutionChain.getHandler());

        return response;
    }

    /**
     * Deserializes the JSON response.
     *
     * @param response
     * @return
     * @throws Exception
     */
    public SimpleObject deserialize(MockHttpServletResponse response) throws Exception {
        return new ObjectMapper().readValue(response.getContentAsString(), SimpleObject.class);
    }

}