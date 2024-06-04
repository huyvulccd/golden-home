package com.example.GoldenHome.components.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

public class Const {
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

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Getter
    public enum JWTStatus {
        ACCEPT_AT((byte) 0),
        INVALID_AT((byte) 1),
        OUT_EXPIRE_AT((byte) 2),
        INVALID_RT((byte) 3),
        NEED_RENEW_RT((byte) 4),
        VALID_RT((byte) 5);

        byte code;
    }

    public final static String regexUsername = "^[a-zA-Z][a-zA-Z0-9]{5,14}$";

}
