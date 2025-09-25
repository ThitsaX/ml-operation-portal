package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.AssignRoleToPrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalRealmIdCommand;
import com.thitsaworks.operation_portal.core.iam.command.RemoveRoleFromPrincipalCommand;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.participant.command.ModifyUserCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyUser;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModifyUserHandler
    extends OperationPortalAuditableUseCase<ModifyUser.Input, ModifyUser.Output>
    implements ModifyUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyUserHandler.class);

    private final ModifyUserCommand modifyUserCommand;

    private final AssignRoleToPrincipalCommand assignRoleToPrincipalCommand;

    private final RemoveRoleFromPrincipalCommand removeRoleFromPrincipalCommand;

    private final ModifyPrincipalRealmIdCommand modifyPrincipalRealmIdCommand;

    private final PrincipalCache principalCache;

    private final UserPermissionManager userPermissionManager;

    private final IAMQuery iamQuery;

    public ModifyUserHandler(CreateInputAuditCommand createInputAuditCommand,
                             CreateOutputAuditCommand createOutputAuditCommand,
                             CreateExceptionAuditCommand createExceptionAuditCommand,
                             ObjectMapper objectMapper,
                             PrincipalCache principalCache,
                             ActionAuthorizationManager actionAuthorizationManager,
                             ModifyUserCommand modifyUserCommand,
                             AssignRoleToPrincipalCommand assignRoleToPrincipalCommand,
                             RemoveRoleFromPrincipalCommand removeRoleFromPrincipalCommand,
                             ModifyPrincipalRealmIdCommand modifyPrincipalRealmIdCommand,
                             UserPermissionManager userPermissionManager,
                             IAMQuery iamQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyUserCommand = modifyUserCommand;
        this.assignRoleToPrincipalCommand = assignRoleToPrincipalCommand;
        this.removeRoleFromPrincipalCommand = removeRoleFromPrincipalCommand;
        this.modifyPrincipalRealmIdCommand = modifyPrincipalRealmIdCommand;
        this.principalCache = principalCache;
        this.userPermissionManager = userPermissionManager;
        this.iamQuery = iamQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();
        var currentUserParticipantId = new ParticipantId(currentUser.realmId()
                                                                    .getId());

        var isDfsp = this.userPermissionManager.isDfsp(currentUser.principalId());
        var roleIdList = input.roleIdList();
        var userId = input.userId();
        var participantId = input.participantId();

        if (isDfsp) {

            if (!this.userPermissionManager.isSameParticipant(currentUserParticipantId, input.participantId())) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }

            if (!this.userPermissionManager.areRolesAllowed(true, roleIdList)) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_ROLE_CREATION);
            }
        }

        ModifyUserCommand.Output output =
            this.modifyUserCommand.execute(new ModifyUserCommand.Input(
                userId,
                input.name(),
                input.firstName(),
                input.lastName(),
                input.jobTitle(),
                participantId));

        var principalId = new PrincipalId(userId.getId());

        this.modifyPrincipalRealmIdCommand.execute(new ModifyPrincipalRealmIdCommand.Input(principalId,
                                                                                           new RealmId(participantId.getId())));

        var
            existingRoleList =
            this.iamQuery.getRolesByPrincipal(principalId)
                         .stream()
                         .map(RoleData::roleId)
                         .collect(Collectors.toSet());

        Set<RoleId> newRoleList = new HashSet<>(roleIdList);

        Set<RoleId> assignRoleList = new HashSet<>(newRoleList);
        assignRoleList.removeAll(existingRoleList);

        Set<RoleId> revokeRoleList = new HashSet<>(existingRoleList);
        revokeRoleList.removeAll(newRoleList);

        for (RoleId role : assignRoleList) {
            this.assignRoleToPrincipalCommand
                .execute(new AssignRoleToPrincipalCommand.Input(principalId, role));
        }

        for (RoleId role : revokeRoleList) {
            this.removeRoleFromPrincipalCommand
                .execute(new RemoveRoleFromPrincipalCommand.Input(principalId, role));
        }
        return new Output(output.modified(), output.userId());

    }

}