package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.data.AuditData;
import com.thitsaworks.operation_portal.core.audit.query.AuditQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.GetMadeBy;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MadeByHandler extends CoreServicesAuditableUseCase<GetMadeBy.Input, GetMadeBy.Output> implements GetMadeBy {

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final AuditQuery auditQuery;

    public MadeByHandler(CreateInputAuditCommand createInputAuditCommand,
                         CreateOutputAuditCommand createOutputAuditCommand,
                         CreateExceptionAuditCommand createExceptionAuditCommand,
                         ObjectMapper objectMapper,
                         PrincipalCache principalCache, AuditQuery auditQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.auditQuery = auditQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        Set<UserId>
            madeByUsers =
            auditQuery.getAudits()
                      .stream()
                      .map(AuditData::userId)
                      .filter(Objects::nonNull)
                      .collect(Collectors.toSet());


        return new Output(madeByUsers.stream()
                                     .map(User::new)
                                     .collect(Collectors.toSet()));
    }

}