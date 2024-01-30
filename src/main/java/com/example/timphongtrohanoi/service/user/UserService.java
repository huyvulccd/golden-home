package com.example.timphongtrohanoi.service.user;

public interface UserService {
    User registerUser(User user);
    String loginUser(String username, String password);
}
