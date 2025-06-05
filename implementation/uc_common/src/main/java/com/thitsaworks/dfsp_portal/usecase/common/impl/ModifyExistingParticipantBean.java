package com.thitsaworks.dfsp_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.dfsp_portal.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.dfsp_portal.iam.query.data.PrincipalData;
import com.thitsaworks.dfsp_portal.participant.domain.command.CreateContact;
import com.thitsaworks.dfsp_portal.participant.domain.command.CreateLiquidityProfile;
import com.thitsaworks.dfsp_portal.participant.domain.command.ModifyContact;
import com.thitsaworks.dfsp_portal.participant.domain.command.ModifyLiquidityProfile;
import com.thitsaworks.dfsp_portal.participant.domain.command.ModifyParticipant;
import com.thitsaworks.dfsp_portal.usecase.common.ModifyExistingParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ModifyExistingParticipantBean extends ModifyExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantBean.class);

    @Autowired
    private ModifyParticipant modifyParticipant;

    @Autowired
    private ModifyContact modifyContact;

    @Autowired
    private CreateContact createContact;

    @Autowired
    private CreateLiquidityProfile createLiquidityProfile;

    @Autowired
    private ModifyLiquidityProfile modifyLiquidityProfile;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @WriteTransactional
    public Output onExecute(Input input) throws Exception {

        PrincipalData principalData = this.principalCache.get(input.getAccessKey());

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.getRealmId() != null &&
                    !principalData.getRealmId().getId().equals(input.getParticipantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        ModifyParticipant.Output output = this.modifyParticipant.execute(
                new ModifyParticipant.Input(input.getParticipantId(),input.getCompanyName(),input.getAddress(), input.getMobile()));

        //For Contact Info
        if (output != null && input.getContactInfoList() != null && !input.getContactInfoList().isEmpty()) {

            for (var contact : input.getContactInfoList()) {

                if (contact.getContactId() != null) {

                    this.modifyContact.execute(
                            new ModifyContact.Input(output.getParticipantId(), contact.getContactId(),
                                    contact.getName(), contact.getTitle(), contact.getEmail(), contact.getMobile()));
                } else {

                    this.createContact.execute(
                            new CreateContact.Input(contact.getName(), contact.getTitle(), contact.getEmail(),
                                    contact.getMobile(), output.getParticipantId(), contact.getContactType()));
                }
            }
        }

        //For Liquidity Profile
        if (output != null && !input.getLiquidityProfileInfoList().isEmpty()) {

            for (var liquidityProfile : input.getLiquidityProfileInfoList()) {

                if (liquidityProfile.getLiquidityProfileId() != null) {

                    this.modifyLiquidityProfile.execute(new ModifyLiquidityProfile.Input(output.getParticipantId(),
                            liquidityProfile.getLiquidityProfileId(), liquidityProfile.getAccountName(),
                            liquidityProfile.getAccountNumber(), liquidityProfile.getCurrency(),
                            liquidityProfile.getIsActive()));
                } else {

                    this.createLiquidityProfile.execute(new CreateLiquidityProfile.Input(output.getParticipantId(),
                            liquidityProfile.getAccountName(), liquidityProfile.getAccountNumber(),
                            liquidityProfile.getCurrency(), liquidityProfile.getIsActive()));

                }
            }
        }

        return new ModifyExistingParticipant.Output(output.isModified(), output.getParticipantId());
    }

    @Override
    protected String getName() {

        return ModifyExistingParticipant.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_common";
    }

    @Override
    protected String getId() {

        return ModifyExistingParticipant.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        switch (principalData.getUserRoleType()) {

            case OPERATION:
            case ADMIN:
                return true;
            case SUPERUSER:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(ModifyExistingParticipant.Input input, ModifyExistingParticipant.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingParticipant.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
