package org.k2.processmining.controller.annotion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nyq on 2017/7/3.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MethodJarsValidator.class)
public @interface MethodJars {
    String message() default "The jars are invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
