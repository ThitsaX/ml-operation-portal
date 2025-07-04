package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipal;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.RemoveParticipantUser;
import com.thitsaworks.operation_portal.usecase.ParticipantAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.participant.RemoveExistingParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemoveExistingParticipantUserBean extends ParticipantAuditableUseCase<RemoveExistingParticipantUser.Input,RemoveExistingParticipantUser.Output> implements RemoveExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final RemoveParticipantUser removeParticipantUser;

    private final ModifyPrincipal modifyPrincipal;

    private final PrincipalCache principalCache;

    public RemoveExistingParticipantUserBean(CreateInputAuditCommand createInputAuditCommand,
                                             CreateOutputAuditCommand createOutputAuditCommand,
                                             CreateExceptionAuditCommand createExceptionAuditCommand,
                                             ObjectMapper objectMapper,
                                             PrincipalCache principalCache,
                                             RemoveParticipantUser removeParticipantUser,
                                             ModifyPrincipal modifyPrincipal) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.removeParticipantUser = removeParticipantUser;
        this.modifyPrincipal = modifyPrincipal;
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId().getId().equals(input.participantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        RemoveParticipantUser.Output output = this.removeParticipantUser.execute(
            new RemoveParticipantUser.Input(input.participantId(), input.participantUserId()));

        this.modifyPrincipal.execute(
            new ModifyPrincipal.Input(new PrincipalId(output.participantUserId().getId()),
                                      PrincipalStatus.INACTIVE));

        return new RemoveExistingParticipantUser.Output(output.removed(), output.participantUserId());

    }

}
