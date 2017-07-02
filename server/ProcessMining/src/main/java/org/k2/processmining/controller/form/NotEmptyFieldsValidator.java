package org.k2.processmining.controller.form;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by nyq on 2017/7/2.
 */
public class NotEmptyFieldsValidator implements ConstraintValidator<NotEmptyFields, List<String>> {

    @Override
    public void initialize(NotEmptyFields notEmptyFields) {
    }

    @Override
    public boolean isValid(List<String> objects, ConstraintValidatorContext context) {
        return objects != null && objects.size() > 0 && objects.stream().allMatch(nef -> !nef.trim().isEmpty());
    }

}

