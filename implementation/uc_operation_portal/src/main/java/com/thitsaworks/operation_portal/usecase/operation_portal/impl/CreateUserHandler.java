package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.AssignRoleToPrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.core.participant.command.CreateUserCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateUserHandler
    extends OperationPortalAuditableUseCase<CreateUser.Input, CreateUser.Output>
    implements CreateUser {

    private static final Logger LOG = LoggerFactory.getLogger(CreateUserHandler.class);

    private final CreateUserCommand createUserCommand;

    private final CreatePrincipalCommand createPrincipalCommand;

    private final AssignRoleToPrincipalCommand assignRoleToPrincipalCommand;

    private final PrincipalCache principalCache;

    private final PrincipalRoleQuery principalRoleQuery;

    private final RoleQuery roleQuery;

    public CreateUserHandler(CreateInputAuditCommand createInputAuditCommand,
                             CreateOutputAuditCommand createOutputAuditCommand,
                             CreateExceptionAuditCommand createExceptionAuditCommand,
                             ObjectMapper objectMapper,
                             PrincipalCache principalCache,
                             ActionAuthorizationManager actionAuthorizationManager,
                             CreateUserCommand createUserCommand,
                             CreatePrincipalCommand createPrincipalCommand,
                             AssignRoleToPrincipalCommand assignRoleToPrincipalCommand,
                             PrincipalCache principalCache1,
                             PrincipalRoleQuery principalRoleQuery,
                             RoleQuery roleQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.createUserCommand = createUserCommand;
        this.createPrincipalCommand = createPrincipalCommand;
        this.assignRoleToPrincipalCommand = assignRoleToPrincipalCommand;
        this.principalCache = principalCache1;
        this.principalRoleQuery = principalRoleQuery;
        this.roleQuery = roleQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);

        }

        var principalRole = this.principalRoleQuery.getRole(principalData.principalId());

        var role = this.roleQuery.get(principalRole.roleId());
        if (role.isDfsp()) {

            if (!input.participantId()
                      .equals(new ParticipantId(principalData.realmId()
                                                             .getId()))) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }

        }

        CreateUserCommand.Output output = this.createUserCommand.execute(
            new CreateUserCommand.Input(input.name(), input.email(), input.participantId(),
                                        input.firstName(), input.lastName(), input.jobTitle()));

        var principalId = new PrincipalId(output.userId()
                                                .getId());

        this.createPrincipalCommand.execute(new CreatePrincipalCommand.Input(principalId,
                                                                             input.password(),
                                                                             new RealmId(input.participantId()
                                                                                              .getId()),
                                                                             input.activeStatus()));

        this.assignRoleToPrincipalCommand.execute(new AssignRoleToPrincipalCommand.Input(principalId, input.roleId()));

        return new Output(output.created());
    }

}

