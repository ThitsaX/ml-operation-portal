package com.thitsaworks.dfsp_portal.component.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneConverter {
    public static String convertTimeZone(String fromDate, String timezone) throws ParseException {
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

}
