package com.example.timphongtrohanoi.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class UserSignup {
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[@#$%^&+=!])(.{6,})$",
            message = "Mật khẩu không hợp lệ")
    private String password;
    private String rePassword;
    @Email(message = "Địa chỉ email không hợp lệ")
    private String email;

    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại không hợp lệ")
    private String phone;

    public UserSignup(String username, String password, String rePassword, String email, String phone) {
        this.username = username;
        this.password = password;
        this.rePassword = rePassword;
        this.email = email;
        this.phone = phone;
    }

    public UserSignup() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
