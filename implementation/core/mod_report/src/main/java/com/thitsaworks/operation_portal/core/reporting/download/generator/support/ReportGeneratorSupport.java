package com.thitsaworks.operation_portal.core.reporting.download.generator.support;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ReportGeneratorSupport {

    public String requireParam(Map<String, String> params, String key) {

        String value = params.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required parameter: " + key);
        }

        return value;
    }

    public String fileType(String fileType) {

        return fileType == null ? "" : fileType.trim().toLowerCase(Locale.ROOT);
    }

    public String normalizeAllToken(String value) {

        if (value == null) {
            return "All";
        }

        return "all".equalsIgnoreCase(value.trim()) ? "All" : value.trim();
    }


}
