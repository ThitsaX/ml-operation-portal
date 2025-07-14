package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantUserCommand;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.ModifyExistingUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyExistingUserBean
    extends CoreServicesAuditableUseCase<ModifyExistingUser.Input, ModifyExistingUser.Output>
    implements ModifyExistingUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingUserBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final ModifyParticipantUserCommand modifyParticipantUserCommand;

    private final ModifyPrincipalCommand modifyPrincipalCommand;

    public ModifyExistingUserBean(CreateInputAuditCommand createInputAuditCommand,
                                  CreateOutputAuditCommand createOutputAuditCommand,
                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                  ObjectMapper objectMapper,
                                  PrincipalCache principalCache,
                                  ModifyParticipantUserCommand modifyParticipantUserCommand,
                                  ModifyPrincipalCommand modifyPrincipalCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyParticipantUserCommand = modifyParticipantUserCommand;
        this.modifyPrincipalCommand = modifyPrincipalCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ModifyParticipantUserCommand.Output output =
                this.modifyParticipantUserCommand.execute(new ModifyParticipantUserCommand.Input(
                        input.participantUserId(),
                        input.name(),
                        null,
                        input.firstName(), input.lastName(), input.jobTitle(), null));

        this.modifyPrincipalCommand.execute(
                new ModifyPrincipalCommand.Input(new PrincipalId(output.participantUserId().getId()),
                                                 input.userRoleType(),
                                                 input.principalStatus()));

        return new Output(output.modified(), output.participantUserId());

    }

}