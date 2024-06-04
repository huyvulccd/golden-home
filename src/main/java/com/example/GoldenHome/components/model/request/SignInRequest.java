package com.example.GoldenHome.components.model.request;

import com.example.GoldenHome.components.annotation.PasswordConstraint;
import com.example.GoldenHome.components.annotation.UsernameConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInRequest {
    @UsernameConstraint
    String username;
    @PasswordConstraint
    String password;
    boolean remember;
}
