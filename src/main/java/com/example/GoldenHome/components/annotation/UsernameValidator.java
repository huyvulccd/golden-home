package com.example.GoldenHome.components.annotation;

import com.example.GoldenHome.components.common.MessageCode;
import com.example.GoldenHome.components.common.Regex;
import com.example.GoldenHome.components.util.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {

    private String message;
    private Class<? extends Payload>[] payload;
    private Class<?>[] groups;
    @Override
    public void initialize(UsernameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        message = constraintAnnotation.message();
        groups = constraintAnnotation.groups();
        payload = constraintAnnotation.payload();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username.length() > 15 || username.length() < 8) {
            ValidationUtils.rewriteMessageErrors(constraintValidatorContext,
                    message + MessageCode.USERNAME_INVALID_LENGTH);
            return false;
        }

        boolean isValid = Regex.username.matcher(username).matches();
        if (isValid)
            return true;

        ValidationUtils.rewriteMessageErrors(constraintValidatorContext, message + MessageCode.USERNAME_INVALID_OTHER);
        return false;
    }
}
