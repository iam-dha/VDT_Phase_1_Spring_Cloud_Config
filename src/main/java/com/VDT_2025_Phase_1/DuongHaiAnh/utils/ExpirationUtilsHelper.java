package com.VDT_2025_Phase_1.DuongHaiAnh.utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ExpirationUtilsHelper {
    public static Date calculateExpiration(String expirationStr, Instant baseTime) {
        long amount = Long.parseLong(expirationStr.substring(0, expirationStr.length() - 1));
        char unit = expirationStr.charAt(expirationStr.length() - 1);

        Instant expiration = switch (unit) {
            case 's' -> baseTime.plus(amount, ChronoUnit.SECONDS);
            case 'm' -> baseTime.plus(amount, ChronoUnit.MINUTES);
            case 'h' -> baseTime.plus(amount, ChronoUnit.HOURS);
            case 'd' -> baseTime.plus(amount, ChronoUnit.DAYS);
            case 'w' -> baseTime.plus(amount * 7, ChronoUnit.DAYS);
            default -> throw new IllegalArgumentException("Invalid expiration format: " + expirationStr);
        };

        return Date.from(expiration);
    }

    public static ZonedDateTime calculateExpirationZoned(String expirationStr, ZonedDateTime baseTime) {
        long amount = Long.parseLong(expirationStr.substring(0, expirationStr.length() - 1));
        char unit = expirationStr.charAt(expirationStr.length() - 1);

        return switch (unit) {
            case 's' -> baseTime.plusSeconds(amount);
            case 'm' -> baseTime.plusMinutes(amount);
            case 'h' -> baseTime.plusHours(amount);
            case 'd' -> baseTime.plusDays(amount);
            case 'w' -> baseTime.plusWeeks(amount);
            default -> throw new IllegalArgumentException("Invalid expiration format: " + expirationStr);
        };
    }


}
