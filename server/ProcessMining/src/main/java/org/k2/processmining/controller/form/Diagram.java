package org.k2.processmining.controller.form;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nyq on 2017/7/2.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DiagramValidator.class)
public @interface Diagram {
    String message() default "The type of diagram is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
