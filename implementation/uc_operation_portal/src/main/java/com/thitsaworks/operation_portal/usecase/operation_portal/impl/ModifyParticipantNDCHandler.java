package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantNDCCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyParticipantNDC;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Set;

@Service
public class ModifyParticipantNDCHandler
    extends OperationPortalAuditableUseCase<ModifyParticipantNDC.Input, ModifyParticipantNDC.Output>
    implements ModifyParticipantNDC {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantNDCHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ModifyParticipantNDCCommand modifyParticipantNDCCommand;

    public ModifyParticipantNDCHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       Set<UserRoleType> permittedRoles,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ModifyParticipantNDCCommand modifyParticipantNDCCommand,
                                       ActionAuthorizationManager actionAuthorizationManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              permittedRoles,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyParticipantNDCCommand = modifyParticipantNDCCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        //TODO: To call mojaloop api and calculate ndcamount logic
        ModifyParticipantNDCCommand.Output output =
            this.modifyParticipantNDCCommand.execute(new ModifyParticipantNDCCommand.Input(input.participantNDCId(),
                                                                                           input.ndcPercent(),
                                                                                           input.ndcAmount()));

        return new Output(output.participantNDCId());
    }

}
