package com.thitsaworks.operation_portal.component.misc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneConverter {
    public static String convertTimeZoneOld(String fromDate, String timezone) throws ParseException {
        DateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcFromDate = utcDateFormat.parse(fromDate);

        int hours = Integer.parseInt(timezone.substring(1, 3));
        int minutes = Integer.parseInt(timezone.substring(3));
        int totalMinutes = hours * 60 + minutes;
        if (timezone.charAt(0) != '-') {
            totalMinutes *= -1;
        }
        int offsetMillis = totalMinutes * 60 * 1000;

        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        localCalendar.setTimeInMillis(utcFromDate.getTime() + offsetMillis);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        localDateFormat.setTimeZone(localCalendar.getTimeZone());
        String localFromDate = localDateFormat.format(localCalendar.getTime());

        return localFromDate;
    }

    private static final DateTimeFormatter OUT_UTC =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                             .withZone(ZoneOffset.UTC);


    public static String convertTimeZone(String fromDateUtcZ, String timezone) {

        final Instant baseUtc = Instant.parse(fromDateUtcZ.trim());
        final int offsetSeconds = parseOffsetSeconds(timezone);

        Instant result = baseUtc.minusSeconds(offsetSeconds).minusSeconds(1);

        return OUT_UTC.format(result);
    }

    private static int parseOffsetSeconds(String raw) {

        if (raw == null || raw.trim().isEmpty()) return 0;
        String s = raw.trim();

        int sign = 1;
        if (s.startsWith("+")) { s = s.substring(1); }
        else if (s.startsWith("-")) { sign = -1; s = s.substring(1); }

        s = s.replace(":", "");
        int hours, minutes;

        if (s.length() == 2) {              // HH
            hours = Integer.parseInt(s);
            minutes = 0;
        } else if (s.length() == 4) {       // HHMM
            hours = Integer.parseInt(s.substring(0, 2));
            minutes = Integer.parseInt(s.substring(2, 4));
        } else {
            throw new IllegalArgumentException(
                    "Invalid offset: " + raw + " (use HH, HHMM, +HHMM, -HH:MM, etc.)");
        }

        return sign * (hours * 3600 + minutes * 60);
    }

}
