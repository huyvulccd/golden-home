package com.example.GoldenHome.components.annotation;

import com.example.GoldenHome.components.common.MessageCode;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UsernameValidator.class})
public @interface UsernameConstraint {
    String message() default MessageCode.USERNAME_NOT_ELIGIBLE;
    Class<?>[]  groups() default {};
    Class<? extends Payload>[] payload() default {};
}
