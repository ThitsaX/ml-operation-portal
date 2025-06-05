package com.thitsaworks.dfsp_portal.component.util;

import lombok.Value;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MMDateTimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss O")
                                                                        .withZone(ZoneId.of("GMT+06:30"));

    public static String toString(Instant instant) {

        return FORMATTER.format(instant);

    }

    public static MMLocalTime nowInMyanmar() {

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(("GMT+06:30")));

        return new MMLocalTime(zonedDateTime.getYear(), zonedDateTime.getMonth().getValue(),
                zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(),
                zonedDateTime.getSecond());
    }

    public static  MMLocalTime toMyanmarTime(Instant instant){

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(("GMT+06:30")));

        return new MMLocalTime(zonedDateTime.getYear(), zonedDateTime.getMonth().getValue(),
                zonedDateTime.getDayOfMonth(), zonedDateTime.getHour(), zonedDateTime.getMinute(),
                zonedDateTime.getSecond());
    }

    @Value
    public static class MMLocalTime {

        private int year;

        private int month;

        private int date;

        private int hour;

        private int minute;

        private int second;

    }

}