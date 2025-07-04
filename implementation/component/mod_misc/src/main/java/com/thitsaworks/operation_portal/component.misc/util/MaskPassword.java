package com.thitsaworks.operation_portal.component.misc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MaskPassword {

    public static String maskPassword(ObjectMapper objectMapper, String jsonString) throws Exception {

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        if (jsonNode.isObject()) {

            ObjectNode objectNode = (ObjectNode) jsonNode;

            jsonNode.fieldNames()
                    .forEachRemaining(fieldName -> {
                        if (fieldName.toLowerCase()
                                     .contains("password") || fieldName.toLowerCase()
                                                                       .contains("accesskey") ||
                                fieldName.toLowerCase()
                                         .contains("secretkey")) {
                            objectNode.put(fieldName, "****");
                        }
                    });
        }

        return objectMapper.writeValueAsString(jsonNode);
    }

}
