package org.openmrs.module.appframework.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Support for evaluating small templates, e.g. an url template like "page?pt={{patient.uuid}}"
 */
@Component("appframeworkTemplateFactory")
public class TemplateFactory {

    private Handlebars handlebars = new Handlebars();

    /**
     * Most commonly-used method in this class. Does one-off evaluation of a handlebars template
     *
     * @param template
     * @param context
     * @return
     */
    public String handlebars(String template, Object context) {
        Template handlebarsTemplate = compileHandlebarsTemplate(template);
        try {
            return handlebarsTemplate.apply(context);
        } catch (IOException e) {
            throw new TemplateException("Error evaluating handlebars template: " + template, e);
        }
    }

    /**
     * Compiles a handlebars template for later use. (This is not the primary use case of this class.)
     *
     * @param template
     * @return
     */
    public Template compileHandlebarsTemplate(String template) {
        try {
            return handlebars.compileInline(template);
        } catch (IOException e) {
            throw new TemplateException("Error compiling handlebars template: " + template, e);
        }
    }

}
