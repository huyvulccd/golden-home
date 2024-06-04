package com.example.GoldenHome.components.util;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeUtils {
    static final int secondsInHour = 60 * 60;
    static final int secondsInDay = secondsInHour * 24;
    public static Date getTimeAfterNow(int hour) {
        return new Date(Instant.now().plus(hour, ChronoUnit.HOURS).toEpochMilli());
    }

    public static Date getTimeAfterStartOfDate(int days) {
        LocalDateTime localDateTime = LocalDate.now().atStartOfDay().plusDays(days);
        long instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return new Date(instant);
    }

    public static int calculateSeconds(int minutes, int seconds) {
        return minutes * 60 + seconds;
    }

    public static int calculateSeconds(int hours, int minutes, int seconds) {
        return hours * secondsInHour + calculateSeconds(minutes, seconds);
    }

    public static int calculateSeconds(int days, int hours, int minutes, int seconds) {
        return days * secondsInDay + calculateSeconds(hours, minutes, seconds);
    }

    public static Date now() {
        return new Date();
    }
}
