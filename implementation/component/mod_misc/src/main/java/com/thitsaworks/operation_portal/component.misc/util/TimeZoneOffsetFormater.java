package com.thitsaworks.operation_portal.component.misc.util;

public class TimeZoneOffsetFormater {

    public static String normalizeOffset(String rawOffset) {

        char sign = rawOffset.charAt(0);
        if (sign != '+' && sign != '-') {
            rawOffset = "+" + rawOffset;
            sign = '+';
        } else {
            rawOffset = rawOffset.substring(1);
        }

        // Remove colon if exists (to support +06:30 and +0630 equally)
        rawOffset = rawOffset.replace(":", "");

        // Pad with leading zeros if needed
        if (rawOffset.length() < 4) {
            rawOffset = String.format("%04d", Integer.parseInt(rawOffset));
        }

        // Final normalized offset
        String hours = rawOffset.substring(0, 2);
        String minutes = rawOffset.substring(2, 4);
        return sign + hours + ":" + minutes;
    }

}
