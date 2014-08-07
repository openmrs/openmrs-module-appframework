package org.openmrs.module.appframework.template;

import com.github.jknack.handlebars.Template;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TemplateFactoryTest {

    public static final String TEMPLATE = "Four score and {{x.duration}} {{ x.unit }} ago";
    public static final String EXPECTED_RESULT = "Four score and 7 years ago";

    @Test
    public void testCompileHandlebarsTemplate() throws Exception {
        TemplateFactory templateFactory = new TemplateFactory();
        Template compiled = templateFactory.compileHandlebarsTemplate(TEMPLATE);
        String result = compiled.apply(createContext());
        assertThat(result, is(EXPECTED_RESULT));
    }

    @Test
    public void testEvaluateHandlebarsTemplate() throws Exception {
        TemplateFactory templateFactory = new TemplateFactory();

        String result = templateFactory.handlebars(TEMPLATE, createContext());
        assertThat(result, is(EXPECTED_RESULT));
    }

    private Map<String, Object> createContext() {
        Map<String, Object> x = new HashMap<String, Object>();
        x.put("duration", 7);
        x.put("unit", "years");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("x", x);
        return context;
    }

}