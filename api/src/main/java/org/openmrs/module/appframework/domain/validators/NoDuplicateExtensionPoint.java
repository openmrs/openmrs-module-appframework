package org.openmrs.module.appframework.domain.validators;

import org.openmrs.module.appframework.domain.validators.ValidationErrorMessages;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DuplicateExtensionPointValidator.class)
@Documented
public @interface NoDuplicateExtensionPoint {

    String message() default ValidationErrorMessages.APP_DESCRIPTOR_DUPLICATE_EXT_POINT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
