package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditByParticipantAndUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAuditListHandler extends OperationPortalAuditableUseCase<GetAuditList.Input, GetAuditList.Output>
    implements GetAuditList {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditListHandler.class);

    private final GetAuditByParticipantAndUserQuery getAuditQuery;

    public GetAuditListHandler(CreateInputAuditCommand createInputAuditCommand,
                               CreateOutputAuditCommand createOutputAuditCommand,
                               CreateExceptionAuditCommand createExceptionAuditCommand,
                               ObjectMapper objectMapper,
                               PrincipalCache principalCache,
                               ActionAuthorizationManager actionAuthorizationManager,
                               GetAuditByParticipantAndUserQuery getAuditByParticipantAndUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.getAuditQuery = getAuditByParticipantAndUserQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetAuditByParticipantAndUserQuery.Output output =
            this.getAuditQuery.execute(new GetAuditByParticipantAndUserQuery.Input(input.realmId(),
                                                                                   input.userId(),
                                                                                   input.fromDate(),
                                                                                   input.toDate(),
                                                                                   input.actionName()));

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
