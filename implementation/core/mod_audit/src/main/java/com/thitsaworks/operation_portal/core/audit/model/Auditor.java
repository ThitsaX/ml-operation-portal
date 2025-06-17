package com.thitsaworks.operation_portal.core.audit.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.audit.query.GetActionBy;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.component.common.identifier.RealmId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Auditor {

    private static final Logger LOG = LoggerFactory.getLogger(Auditor.class);

    public static <T> void audit(ObjectMapper objectMapper, Class<T> useCase, Object input, Object output,
                                 UserId actionBy) throws UserNotFoundException {

        CreateAudit createAudit = SpringContext.getBean(CreateAudit.class);

        GetActionBy getActionBy = (GetActionBy) SpringContext.getBean(GetActionBy.class);

        try {

            GetActionBy.Output optionalUser = getActionBy.execute(new GetActionBy.Input(actionBy));

            if (optionalUser == null) {

                throw new UserNotFoundException(actionBy.getId().toString());

            }

            if (objectMapper != null) {
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            }

            String inputInfo = objectMapper.writeValueAsString(input);

            String outputInfo = objectMapper.writeValueAsString(output);

            createAudit.execute(new CreateAudit.Input(useCase.getSimpleName(), actionBy,
                    optionalUser.getUserData().getParticipantId() == null ? null :
                            new RealmId(optionalUser.getUserData().getParticipantId()), inputInfo, outputInfo));

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

}
