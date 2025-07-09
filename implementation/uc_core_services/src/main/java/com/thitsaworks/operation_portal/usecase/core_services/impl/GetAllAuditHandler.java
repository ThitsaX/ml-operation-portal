package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditByParticipantAndUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.GetAllAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllAuditHandler extends CoreServicesAuditableUseCase<GetAllAudit.Input, GetAllAudit.Output>
    implements GetAllAudit {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final GetAuditByParticipantAndUserQuery getAuditQuery;

    public GetAllAuditHandler(CreateInputAuditCommand createInputAuditCommand,
                              CreateOutputAuditCommand createOutputAuditCommand,
                              CreateExceptionAuditCommand createExceptionAuditCommand,
                              ObjectMapper objectMapper,
                              PrincipalCache principalCache,
                              GetAuditByParticipantAndUserQuery getAuditByParticipantAndUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.getAuditQuery = getAuditByParticipantAndUserQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetAuditByParticipantAndUserQuery.Output output =
            this.getAuditQuery.execute(new GetAuditByParticipantAndUserQuery.Input(input.realmId(),
                                                                                   input.userId(),
                                                                                   input.fromDate(),
                                                                                   input.toDate()));

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (GetAuditByParticipantAndUserQuery.Output.AuditInfo data : output.getAuditInfoList()) {

            auditInfoList.add(new Output.AuditInfo(data.getParticipantName(),
                                                   data.getUserName(),
                                                   data.getActionName(),
                                                   data.getInputInfo(),
                                                   data.getOutputInfo(),
                                                   data.getActionDate()));
        }

        return new Output(auditInfoList);
    }

}
