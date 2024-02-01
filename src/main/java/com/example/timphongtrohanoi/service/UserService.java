package com.example.timphongtrohanoi.service;

import com.example.timphongtrohanoi.repositories.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User registerUser(User user);
    String loginUser(String username, String password);

    UserDetailsService userDetailsService();
}
