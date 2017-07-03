package org.k2.processmining.controller.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Created by nyq on 2017/7/3.
 */
public class MethodJarsValidator implements ConstraintValidator<MethodJars, MultipartFile[]> {
    @Override
    public void initialize(MethodJars constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {
        return value != null && value.length > 0 && Arrays.stream(value).anyMatch(file -> file.getOriginalFilename().endsWith("-k2.jar"));
    }
}
