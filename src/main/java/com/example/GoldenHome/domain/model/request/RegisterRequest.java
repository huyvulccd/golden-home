package com.example.GoldenHome.domain.model.request;

import com.example.GoldenHome.domain.model.request.Request;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public class RegisterRequest extends Request {
    String username;
    String password;
    String email;
}
