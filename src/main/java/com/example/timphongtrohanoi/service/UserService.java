package com.example.timphongtrohanoi.service;

import com.example.timphongtrohanoi.controller.model.UserSignup;
import com.example.timphongtrohanoi.repositories.entities.User;
import com.example.timphongtrohanoi.service.dto.Validate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;

public interface UserService {

    User registerUser(User user);
    String loginUser(String username, String password);

    UserDetailsService userDetailsService();

    Validate doValidateFormSubmit(UserSignup userSignup, BindingResult bindingResult);
}
