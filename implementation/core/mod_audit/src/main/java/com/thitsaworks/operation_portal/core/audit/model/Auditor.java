package com.thitsaworks.operation_portal.core.audit.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.spring.SpringContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.query.GetActionByQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Auditor {

    private static final Logger LOG = LoggerFactory.getLogger(Auditor.class);

    public static <T> void audit(ObjectMapper objectMapper, Class<T> useCase, Object input, Object output,
                                 UserId actionBy) throws UserNotFoundException {

        CreateAudit createAudit = SpringContext.getBean(CreateAudit.class);

        GetActionByQuery getActionByQuery = (GetActionByQuery) SpringContext.getBean(GetActionByQuery.class);

        try {

            GetActionByQuery.Output optionalUser = getActionByQuery.execute(new GetActionByQuery.Input(actionBy));

            if (optionalUser == null) {

                throw new UserNotFoundException(actionBy.getId().toString());

            }

            if (objectMapper != null) {
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            }

            String inputInfo = objectMapper.writeValueAsString(input);

            String outputInfo = objectMapper.writeValueAsString(output);

            createAudit.execute(new CreateAudit.Input(useCase.getSimpleName(), actionBy,
                                                      optionalUser.userData().getParticipantId() == null ? null :
                                                              new RealmId(optionalUser.userData().getParticipantId()),
                                                      inputInfo,
                                                      outputInfo));

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

}
