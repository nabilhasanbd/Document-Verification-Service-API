package com.example.documentverification.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtils {
    
    public String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        return null;
    }
    
    public LocalDateTime parseStringToLocalDateTime(String dateTimeString, String pattern) {
        return null;
    }
    
    public boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    public boolean isPastDate(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }
}