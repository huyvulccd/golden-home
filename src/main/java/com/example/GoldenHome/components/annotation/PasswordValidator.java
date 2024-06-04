package com.example.GoldenHome.components.annotation;

import com.example.GoldenHome.components.common.MessageCode;
import com.example.GoldenHome.components.common.Regex;
import com.example.GoldenHome.components.util.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    private String message;

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if (password.length() > 15 || password.length() < 8) {
            ValidationUtils.rewriteMessageErrors(constraintValidatorContext, message + MessageCode.PASSWORD_INVALID_LENGTH);
            return false;
        }

        boolean isValid = Regex.password.matcher(password).matches();
        if (isValid)
            return true;

        ValidationUtils.rewriteMessageErrors(constraintValidatorContext, message + MessageCode.PASSWORD_INVALID_OTHER);
        return false;
    }
}
