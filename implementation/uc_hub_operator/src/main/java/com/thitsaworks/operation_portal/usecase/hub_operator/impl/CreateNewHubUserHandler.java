package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateHubUser;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipal;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewHubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewHubUserHandler
    extends HubOperatorAuditableUseCase<CreateNewHubUser.Input, CreateNewHubUser.Output> implements CreateNewHubUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewHubUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.SUPERUSER,
                                                                    UserRoleType.ADMIN,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.OPERATION);

    private final CreateHubUser createHubUser;

    private final CreatePrincipal createPrincipal;

    public CreateNewHubUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   CreateHubUser createHubUser,
                                   CreatePrincipal createPrincipal) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createHubUser = createHubUser;
        this.createPrincipal = createPrincipal;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        CreateHubUser.Output output =
            this.createHubUser.execute(
                new CreateHubUser.Input(input.name(), input.email(),
                                        input.firstName(), input.lastName(), input.jobTitle()));

        CreatePrincipal.Output createAccessOutput = this.createPrincipal.execute(
            new CreatePrincipal.Input(new PrincipalId(output.hubUserId()
                                                            .getId()), RealmType.HUB_OPERATOR,
                                      input.password(), null, input.userRoleType(), input.activeStatus()));

        return new CreateNewHubUser.Output(output.hubUserId(), createAccessOutput.accessKey(),
                                           createAccessOutput.secretKey());
    }

}