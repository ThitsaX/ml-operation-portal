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

    //    Keeps negative sign.
    //    Drops positive “+”.
    //    Removes : if present.
    //    Pads to 4 digits for consistency.
    //    Converts Z/UTC/GMT → 0000.
    public static String normalizeOffsetFormat(String input) {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Offset cannot be null or blank");
        }

        String s = input.trim().toUpperCase().trim();

        // Handle UTC aliases
        if (s.equals("Z") || s.equals("UTC") || s.equals("GMT")) {
            return "0000";
        }

        // Extract sign if negative
        boolean negative = s.startsWith("-");
        if (negative || s.startsWith("+")) {
            s = s.substring(1);
        }

        // Remove any colon (e.g., "06:30" → "0630")
        s = s.replace(":", "");

        // Validate numeric part
        if (!s.matches("\\d{1,4}")) {
            throw new IllegalArgumentException("Invalid offset format: " + input);
        }

        // Normalize to 4 digits (HHmm)
        if (s.length() == 1) {
            s = "0" + s + "00";   // "8"   → "0800"
        } else if (s.length() == 2) {
            s = s + "00";    // "06"  → "0600"
        } else if (s.length() == 3) {
            s = "0" + s;     // "630" → "0630"
        }
        // if already 4, keep as-is

        return negative ? "-" + s : s;
    }


}
