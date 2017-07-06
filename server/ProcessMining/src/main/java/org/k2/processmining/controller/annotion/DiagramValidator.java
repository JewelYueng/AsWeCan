package org.k2.processmining.controller.annotion;

import org.k2.processmining.support.mining.model.DiagramType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Created by nyq on 2017/7/2.
 */
public class DiagramValidator implements ConstraintValidator<Diagram, String> {
    @Override
    public void initialize(Diagram constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(DiagramType.values()).anyMatch(s->s.getValue().equals(value));
    }
}
