package com.example.timphongtrohanoi.service.impl;

import com.example.timphongtrohanoi.service.AccountService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AccountServiceImpl implements AccountService {


    private BCryptPasswordEncoder passwordEncoder;

    public String loginUser(String username, String password) {
        // Implement JWT generation logic here
        // Return the generated token
        return "JWT_TOKEN";
    }
}
