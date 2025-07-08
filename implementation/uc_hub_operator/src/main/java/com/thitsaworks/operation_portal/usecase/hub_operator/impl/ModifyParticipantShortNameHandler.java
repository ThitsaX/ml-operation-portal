package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCompanyShortNameCommand;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyParticipantShortName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyParticipantShortNameHandler
    extends HubOperatorAuditableUseCase<ModifyParticipantShortName.Input, ModifyParticipantShortName.Output>
    implements ModifyParticipantShortName {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantShortNameHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN, UserRoleType.OPERATION);

    private final ModifyParticipantCompanyShortNameCommand modifyParticipant;

    private final PrincipalCache principalCache;

    @Autowired
    public ModifyParticipantShortNameHandler(CreateInputAuditCommand createInputAuditCommand,
                                             CreateOutputAuditCommand createOutputAuditCommand,
                                             CreateExceptionAuditCommand createExceptionAuditCommand,
                                             ModifyParticipantCompanyShortNameCommand modifyParticipant,
                                             ObjectMapper objectMapper,
                                             PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyParticipant = modifyParticipant;
        this.principalCache = principalCache;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        PrincipalData principalData = this.principalCache.get(input.accessKey());

        if (principalData == null) {

            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId()
                                  .getId()
                                  .equals(input.participantId()
                                               .getId())) {

                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }
        }

        ModifyParticipantCompanyShortNameCommand.Output output = this.modifyParticipant.execute(
            new ModifyParticipantCompanyShortNameCommand.Input(input.participantId(), input.companyShortName()));

        return new Output(output.modified(), output.participantId());
    }

}
