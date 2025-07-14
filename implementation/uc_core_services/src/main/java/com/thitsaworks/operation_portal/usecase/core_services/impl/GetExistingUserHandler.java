package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;

import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.GetExistingUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetExistingUserHandler
    extends CoreServicesAuditableUseCase<GetExistingUser.Input, GetExistingUser.Output>
    implements GetExistingUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final HubUserQuery hubUserQuery;

    @Autowired
    public GetExistingUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                  CreateOutputAuditCommand createOutputAuditCommand,
                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                  HubUserQuery hubUserQuery,
                                  ObjectMapper objectMapper,
                                  PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.hubUserQuery = hubUserQuery;
    }

    @Override
    public GetExistingUser.Output onExecute(GetExistingUser.Input input) throws DomainException {

        HubUserData hubUserData = this.hubUserQuery.get(null);

        return new GetExistingUser.Output(null,
                                             hubUserData.name(),
                                             hubUserData.email(),
                                             hubUserData.firstName(),
                                             hubUserData.lastName(),
                                             hubUserData.jobTitle(),
                                          null,null,null);
    }

}
