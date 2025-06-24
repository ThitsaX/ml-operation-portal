package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipant;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewParticipantHandler extends CreateNewParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantHandler.class);

    private final CreateParticipant createParticipant;

    private final CreateContact createContact;

    private final CreateLiquidityProfile createLiquidityProfile;

    private final ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws Exception {

        CreateParticipant.Output output = this.createParticipant.execute(
                new CreateParticipant.Input(input.name(), input.dfspCode(), input.dfspName(),
                                            input.address(), input.mobile()));

        if (output != null && (input.contactInfoList() != null || !input.contactInfoList().isEmpty()) &&
                input.contactInfoList().size() > 0) {

            for (var contact : input.contactInfoList()) {
                this.createContact.execute(
                        new CreateContact.Input(contact.name(), contact.title(), contact.email(),
                                                contact.mobile(), output.participantId(), contact.contactType()));
            }
        }

        //For Liquidity Profile
        if (output != null &&
                (input.liquidityProfileInfoList() != null || !input.liquidityProfileInfoList().isEmpty()) &&
                input.liquidityProfileInfoList().size() > 0) {

            for (var liquidityProfile : input.liquidityProfileInfoList()) {

                this.createLiquidityProfile.execute(new CreateLiquidityProfile.Input(output.participantId(),
                                                                                     liquidityProfile.accountName(),
                                                                                     liquidityProfile.accountNumber(),
                                                                                     liquidityProfile.currency(),
                                                                                     liquidityProfile.isActive()));
            }
        }

        return new CreateNewParticipant.Output(output.created(), output.participantId());
    }

    @Override
    protected String getName() {

        return CreateNewParticipant.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_hub_operator";
    }

    @Override
    protected String getId() {

        return CreateNewParticipant.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

    @Override
    public void onAudit(Input input, Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper,
                      CreateNewParticipant.class,
                      input,
                      output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
