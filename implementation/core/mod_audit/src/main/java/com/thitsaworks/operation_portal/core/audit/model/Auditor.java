package com.thitsaworks.operation_portal.core.audit.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

            String inputInfo = objectMapper.writeValueAsString(input);

            String outputInfo = objectMapper.writeValueAsString(output);

            createAudit.execute(new CreateAudit.Input(useCase.getSimpleName(), userId, realmId, inputInfo, outputInfo));

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

}
