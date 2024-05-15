package com.example.timphongtrohanoi.domain.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class SignInResponse {
    String token;
    Map<String, String> errors;

    public String add(String field, String error) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        return errors.put(field, error);
    }
}
