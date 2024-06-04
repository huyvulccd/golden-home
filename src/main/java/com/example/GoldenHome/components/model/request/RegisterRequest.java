package com.example.GoldenHome.components.model.request;

import com.example.GoldenHome.components.annotation.EmailConstraint;
import com.example.GoldenHome.components.annotation.PasswordConstraint;
import com.example.GoldenHome.components.annotation.UsernameConstraint;
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
    @UsernameConstraint
    String username;
    @PasswordConstraint
    String password;
    @EmailConstraint
    String email;
}
