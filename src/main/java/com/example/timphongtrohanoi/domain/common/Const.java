package com.example.timphongtrohanoi.domain.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class Const {

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Getter
    public enum MessageCode {
        SIGNUP_USERNAME_EXISTED("Username đã tồn tại"),
        SIGNUP_USERNAME_INVALID("phải dài hơn 6 ký tự"),
        SIGNUP_PASSWORD_INVALID("Password phải dài hơn 6 kí tự, chữ hoa, chữ thường số và kí tự đặc biệt"),
        SIGNUP_RE_PASSWORD_NOT_SAME("Username đã tồn tại"),
        SIGNUP_EMAIL_INVALID("Email dùng domain [@gmail.com]"),
        SIGNUP_PHONE_NUMBER_INVALID("Số điện thoại không hợp lệ"),
        LOGIN_USER_NOT_EXISTED("Tên đăng nhập hoặc mật khẩu sai"),
        NOT_AUTHENTICATION("Incorrect password");

        String message;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Getter
    public enum Role {
        ADMIN((byte) 0, "admin"),
        RENTAL((byte) 1, "rental"),
        TENANT((byte) 2, "tenant");

        byte code;
        String name;
    }
}
