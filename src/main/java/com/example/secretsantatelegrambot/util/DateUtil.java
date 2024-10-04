package com.example.secretsantatelegrambot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final String DATE_PATTERN = "dd.MM.yyyy";

    public static String getStringFromDate(LocalDateTime date) {
        return getStringFromDate(date , DATE_PATTERN);
    }

    public static String getStringFromDate(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime getDateFromString(String date) {
        return getDateFromString(date, DATE_PATTERN);
    }

    public static LocalDateTime getDateFromString(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
