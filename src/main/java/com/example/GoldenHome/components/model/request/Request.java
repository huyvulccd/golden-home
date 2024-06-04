package com.example.GoldenHome.components.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {
    Map<String, String> errors;
    boolean valid;
}
