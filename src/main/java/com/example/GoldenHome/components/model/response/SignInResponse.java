package com.example.GoldenHome.components.model.response;

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
    HttpStatus status;
    String accessToken;
    String refreshToken;
    String hashPsw;
    Map<String, String> errors;

    public void add(String field, String error) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(field, error);
    }

    public boolean hasError() {
        return errors != null && !errors.isEmpty();
    }
}
