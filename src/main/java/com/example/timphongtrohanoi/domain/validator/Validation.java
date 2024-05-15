package com.example.timphongtrohanoi.domain.validator;

import com.example.timphongtrohanoi.domain.model.request.Request;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Validation {

    public List<String> getErrorMessage(Request Object) {
        List<String> fieldsPriority = new ArrayList<>();
        fieldsPriority.add("SYSTEM");
        fieldsPriority.addAll(setFieldsPriority());

        return sortMessages(validator(Object), fieldsPriority);
    }

    private List<String> sortMessages(Map<String, String> request, List<String> fieldsPriority) {
        List<String> errorMessages = new ArrayList<>();

        fieldsPriority.forEach(field -> {
            String message = request.get(field);
            if (message != null)
                errorMessages.add(message);
        });
        return errorMessages;
    }

    public abstract List<String> setFieldsPriority();
    public abstract <T extends Request> Map<String, String> validator(T object);
}
