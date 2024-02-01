package com.example.timphongtrohanoi.common.enums;

public enum Role {
    ADMIN("admin"),
    RENTAL("rental"),
    TENANT("tenant");
    private final String name;
    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
