package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.participant.domain.command.CreateContact;
import com.thitsaworks.operation_portal.participant.domain.command.CreateLiquidityProfile;
import com.thitsaworks.operation_portal.participant.domain.command.CreateParticipant;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateNewParticipantBean extends CreateNewParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantBean.class);

    @Autowired
    private CreateParticipant createParticipant;

    @Autowired
    private CreateContact createContact;

    @Autowired
    private CreateLiquidityProfile createLiquidityProfile;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public Output onExecute(Input input) throws Exception {

        CreateParticipant.Output output = this.createParticipant.execute(
                new CreateParticipant.Input(input.getName(), input.getDfspCode(), input.getDfspName(),
                        input.getAddress(), input.getMobile()));

        if (output != null && (input.getContactInfoList() != null || !input.getContactInfoList().isEmpty()) &&
                input.getContactInfoList().size() > 0) {

            for (var contact : input.getContactInfoList()) {
                this.createContact.execute(
                        new CreateContact.Input(contact.getName(), contact.getTitle(), contact.getEmail(),
                                contact.getMobile(), output.getParticipantId(), contact.getContactType()));
            }
        }

        //For Liquidity Profile
        if (output != null &&
                (input.getLiquidityProfileInfoList() != null || !input.getLiquidityProfileInfoList().isEmpty()) &&
                input.getLiquidityProfileInfoList().size() > 0) {

            for (var liquidityProfile : input.getLiquidityProfileInfoList()) {

                this.createLiquidityProfile.execute(new CreateLiquidityProfile.Input(output.getParticipantId(),
                        liquidityProfile.getAccountName(), liquidityProfile.getAccountNumber(),
                        liquidityProfile.getCurrency(), liquidityProfile.getIsActive()));
            }
        }

        return new CreateNewParticipant.Output(output.isCreated(), output.getParticipantId());
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

        Auditor.audit(this.objectMapper, CreateNewParticipant.class, input, output, new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
