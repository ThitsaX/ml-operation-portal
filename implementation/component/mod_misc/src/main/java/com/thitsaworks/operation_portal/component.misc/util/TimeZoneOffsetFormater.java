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

        rawOffset = rawOffset.replace(":", "");

        if (rawOffset.length() < 4) {
            rawOffset = String.format("%04d", Integer.parseInt(rawOffset));
        }

        String hours = rawOffset.substring(0, 2);
        String minutes = rawOffset.substring(2, 4);

        return sign + hours + ":" + minutes;
    }

}
