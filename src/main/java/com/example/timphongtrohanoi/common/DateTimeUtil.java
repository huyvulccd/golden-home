package com.example.timphongtrohanoi.common;

import java.util.Date;

public class DateTimeUtil {

    public static int quantityMillisecondInHour = 3600000;

    public static Date atTime() {
        return new Date(System.currentTimeMillis());
    }

    public static Date nextHour() {
        return new Date(System.currentTimeMillis() + quantityMillisecondInHour);
    }
}
