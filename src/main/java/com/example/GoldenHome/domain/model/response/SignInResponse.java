package com.example.GoldenHome.domain.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class SignInResponse {
    private int status;
    String token;
    Map<String, String> errors;

    public void add(String field, String error) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(field, error);
    }

    public HttpStatus getStatus() {
        return HttpStatus.valueOf(status);
    }
}
