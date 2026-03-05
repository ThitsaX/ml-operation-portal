package com.thitsaworks.operation_portal.core.report_download.request;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;

final class ReportRequestSignature {

    private ReportRequestSignature() {

    }

    static String from(String reportType, String fileType, Map<String, String> params) {

        StringBuilder canonical = new StringBuilder();
        canonical.append(normalize(reportType))
                 .append("|")
                 .append(normalize(fileType));

        params.entrySet()
              .stream()
              .sorted(Comparator.comparing(Map.Entry::getKey))
              .forEach(entry -> canonical.append("|")
                                         .append(entry.getKey())
                                         .append("=")
                                         .append(normalize(entry.getValue())));

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(canonical.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);

            for (byte value : hash) {

                hex.append(String.format("%02x", value));
            }

            return hex.toString();

        } catch (Exception e) {

            throw new IllegalStateException("Unable to generate request signature", e);
        }
    }

    private static String normalize(String value) {

        return value == null ? "" : value.trim();
    }
}
