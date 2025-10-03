package com.thitsaworks.operation_portal.component.misc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MaskPassword {

    private static final String MASK = "****";

    private static final Set<String> SENSITIVE_FIELDS = new HashSet<>();

    static {
        SENSITIVE_FIELDS.add("password");
        SENSITIVE_FIELDS.add("secretkey");
        SENSITIVE_FIELDS.add("accesskey");
    }

    public static String maskPassword(ObjectMapper objectMapper, String jsonString) throws Exception {

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        if (jsonNode.isObject()) {

            ObjectNode objectNode = (ObjectNode) jsonNode;

            jsonNode.fieldNames()
                    .forEachRemaining(fieldName -> {
                        if (SENSITIVE_FIELDS.stream()
                                            .anyMatch(fieldName.toLowerCase()::contains)) {
                            objectNode.put(fieldName, MASK);
                        }
                    });
        }

        return objectMapper.writeValueAsString(jsonNode);
    }

    public static String toMaskedString(Object obj) {

        if (obj == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        Class<?> clazz = obj.getClass();
        sb.append(clazz.getSimpleName())
          .append("[");

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                String fieldName = field.getName();

                sb.append(fieldName)
                  .append("=");

                if (SENSITIVE_FIELDS.stream()
                                    .anyMatch(fieldName.toLowerCase()::contains)) {
                    sb.append(MASK);
                } else {
                    sb.append(value);
                }
            } catch (IllegalAccessException e) {
                sb.append("ERROR");
            }

            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

}
