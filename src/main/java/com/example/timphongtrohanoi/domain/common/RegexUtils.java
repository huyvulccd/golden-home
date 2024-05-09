package com.example.timphongtrohanoi.domain.common;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    public static boolean checkPassword(String password) {
        if (StringUtils.isEmpty(password)) return false;

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean checkEmail(String email) {
        if (StringUtils.isEmpty(email)) return false;
        String regex = "\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) return false;

        phoneNumber = phoneNumber.replaceAll("\\s", "");

        String regex = "^(84|\\+84|0)?(\\d{9})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
