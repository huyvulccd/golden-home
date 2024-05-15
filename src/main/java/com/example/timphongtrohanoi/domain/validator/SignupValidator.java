package com.example.timphongtrohanoi.domain.validator;

import com.example.timphongtrohanoi.domain.model.request.Request;

import java.util.List;
import java.util.Map;

public class SignupValidator extends Validation {
    @Override
    public List<String> setFieldsPriority() {
        return List.of("username",
                "password",
                "email");
    }

    @Override
    public <T extends Request> Map<String, String> validator(T object) {
        return Map.of();
    }

}
