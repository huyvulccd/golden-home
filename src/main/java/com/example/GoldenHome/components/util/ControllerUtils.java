package com.example.GoldenHome.components.util;

import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerUtils {
    public static ResponseEntity<Object> doCheckValidFields(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(objectError -> {
                errors.put(((FieldError) objectError).getField(), objectError.getDefaultMessage());
            });
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    public static String getToken(Cookie[] cookies, String keyCookie) {
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), keyCookie)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
