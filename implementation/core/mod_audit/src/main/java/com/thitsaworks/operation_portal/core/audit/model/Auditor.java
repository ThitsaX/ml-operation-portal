package com.thitsaworks.operation_portal.core.audit.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Auditor {

    private static final Logger LOG = LoggerFactory.getLogger(Auditor.class);

    public static <T> void audit(ObjectMapper objectMapper, Class<T> useCase, Object input, Object output,
                                 UserId userId, RealmId realmId) {

        CreateAudit createAudit = SpringContext.getBean(CreateAudit.class);

        try {

            if (objectMapper != null) {
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            }

            String inputJson = objectMapper.writeValueAsString(input);
            String inputInfo = maskPassword(objectMapper, inputJson);

            String outputJson = objectMapper.writeValueAsString(output);
            String outputInfo = maskPassword(objectMapper, outputJson);

            createAudit.execute(new CreateAudit.Input(useCase.getSimpleName(), userId, realmId, inputInfo, outputInfo));

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public static String maskPassword(ObjectMapper objectMapper, String jsonString) throws Exception {

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        if (jsonNode.isObject()) {

            ObjectNode objectNode = (ObjectNode) jsonNode;

            jsonNode.fieldNames().forEachRemaining(fieldName -> {
                if (fieldName.toLowerCase().contains("password") || fieldName.toLowerCase().contains("accesskey") ||
                        fieldName.toLowerCase().contains("secretkey")) {
                    objectNode.put(fieldName, "****");
                }
            });
        }

        return objectMapper.writeValueAsString(jsonNode);
    }

}
