package com.example.timphongtrohanoi.controller.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserSignup {

    private String username;
    private String password;
    private String rePassword;
    private String email;
    private String phone;
}
