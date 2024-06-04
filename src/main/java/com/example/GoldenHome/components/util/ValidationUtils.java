package com.example.GoldenHome.components.util;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class ValidationUtils {
    public static void rewriteMessageErrors(ConstraintValidatorContext constraintValidatorContext, String message) {
        HibernateConstraintValidatorContext context = constraintValidatorContext
                .unwrap(HibernateConstraintValidatorContext.class);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
