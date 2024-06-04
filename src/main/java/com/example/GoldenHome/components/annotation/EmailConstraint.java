package com.example.GoldenHome.components.annotation;

import com.example.GoldenHome.components.common.MessageCode;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EmailValidator.class})
public @interface EmailConstraint {
    String message() default MessageCode.EMAIL_NOT_ELIGIBLE;
    Class<?>[]  groups() default {};
    Class<? extends Payload>[] payload() default {};
}
