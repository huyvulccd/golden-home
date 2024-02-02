package com.example.timphongtrohanoi.service.impl;

import com.example.timphongtrohanoi.controller.model.UserSignup;
import com.example.timphongtrohanoi.repositories.UserRepository;
import com.example.timphongtrohanoi.repositories.entities.User;
import com.example.timphongtrohanoi.service.UserService;
import com.example.timphongtrohanoi.service.dto.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public String loginUser(String username, String password) {
        // Implement JWT generation logic here
        // Return the generated token
        return "JWT_TOKEN";
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username);
    }

    @Override
    public Validate doValidateFormSubmit(UserSignup userSignup, BindingResult bindingResult) {
        Validate validate = new Validate();

        validate.addMessage("kjd");
        return validate;
    }
}
