package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipal;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantUser;
import com.thitsaworks.operation_portal.usecase.ParticipantAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.participant.ModifyExistingParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyExistingParticipantUserBean
    extends ParticipantAuditableUseCase<ModifyExistingParticipantUser.Input, ModifyExistingParticipantUser.Output>
    implements ModifyExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantUserBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final ModifyParticipantUser modifyParticipantUser;

    private final ModifyPrincipal modifyPrincipal;

    public ModifyExistingParticipantUserBean(CreateInputAuditCommand createInputAuditCommand,
                                             CreateOutputAuditCommand createOutputAuditCommand,
                                             CreateExceptionAuditCommand createExceptionAuditCommand,
                                             ObjectMapper objectMapper,
                                             PrincipalCache principalCache,
                                             ModifyParticipantUser modifyParticipantUser,
                                             ModifyPrincipal modifyPrincipal) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyParticipantUser = modifyParticipantUser;
        this.modifyPrincipal = modifyPrincipal;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ModifyParticipantUser.Output output = this.modifyParticipantUser.execute(
            new ModifyParticipantUser.Input(input.participantUserId(), input.name(), input.email(),
                                            input.firstName(), input.lastName(), input.jobTitle(), null));

        this.modifyPrincipal.execute(
            new ModifyPrincipal.Input(new PrincipalId(output.participantUserId()
                                                            .getId()), input.userRoleType(),
                                      input.principalStatus()));

        return new Output(output.modified(), output.participantUserId());

    }

}