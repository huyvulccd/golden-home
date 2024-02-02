package com.example.timphongtrohanoi.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Validate {
    private boolean isValid = true;
    private List<String> messages;

    public void addMessage(String message) {
        if (Objects.isNull(messages)) {
            messages = new ArrayList<>();
        }

        if (messages.contains(message))
            throw new RuntimeException("Duplicate message in validate form");

        messages.add(message);
        isValid = false;
    }

    public boolean hasErrors() {
        return !isValid;
    }

    public List<String> getMessages() {
        return messages;
    }
}
