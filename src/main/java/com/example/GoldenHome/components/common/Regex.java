package com.example.GoldenHome.components.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Regex {
    username("^[a-zA-Z][a-zA-Z0-9]{8,15}$"),
    password("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,15}$"),
    email("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    Regex(String regex) {
        pattern = Pattern.compile(regex);
    }
    final Pattern pattern;

    public Matcher matcher(String username) {
        return pattern.matcher(username);
    }
}
