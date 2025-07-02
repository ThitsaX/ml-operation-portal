package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.core.iam.exception.UnauthorizedCreationException;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContact;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipant;
import com.thitsaworks.operation_portal.core.participant.model.Contact;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyExistingParticipantHandler extends ModifyExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantHandler.class);

    private final ModifyParticipant modifyParticipant;

    private final ModifyContact modifyContact;

    private final CreateContact createContact;

    private final CreateLiquidityProfile createLiquidityProfile;

    private final ModifyLiquidityProfile modifyLiquidityProfile;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public Output onExecute(Input input) throws Exception {

        PrincipalData principalData = this.principalCache.get(input.accessKey());

        if (principalData == null) {

            throw new PrincipalNotFoundException();

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId().getId().equals(input.participantId().getId())) {

                throw new UnauthorizedCreationException();
            }
        }

        ModifyParticipant.Output output = this.modifyParticipant.execute(
                new ModifyParticipant.Input(input.participantId(),
                                            input.companyName(),
                                            input.address(),
                                            input.mobile()));

        //For Contact Info
        if (output != null && input.contactInfoList() != null && !input.contactInfoList().isEmpty()) {

            for (var contact : input.contactInfoList()) {

                if (contact.contactId() != null) {

                    this.modifyContact.execute(
                            new ModifyContact.Input(output.participantId(),
                                                    contact.contactId(),
                                                    contact.name(),
                                                    contact.title(),
                                                    contact.email(),
                                                    contact.mobile(),
                                                    contact.contactType()));
                } else {

                    this.createContact.execute(
                            new CreateContact.Input(contact.name(),
                                                    contact.title(),
                                                    contact.email(),
                                                    contact.mobile(),
                                                    output.participantId(),
                                                    contact.contactType()));
                }
            }
        }

        //For Liquidity Profile
        if (output != null && !input.liquidityProfileInfoList().isEmpty()) {

            for (var liquidityProfile : input.liquidityProfileInfoList()) {

                if (liquidityProfile.liquidityProfileId() != null) {

                    this.modifyLiquidityProfile.execute(new ModifyLiquidityProfile.Input(output.participantId(),
                                                                                         liquidityProfile.liquidityProfileId(),
                                                                                         liquidityProfile.accountName(),
                                                                                         liquidityProfile.accountNumber(),
                                                                                         liquidityProfile.currency(),
                                                                                         liquidityProfile.isActive()));
                } else {

                    this.createLiquidityProfile.execute(new CreateLiquidityProfile.Input(output.participantId(),
                                                                                         liquidityProfile.accountName(),
                                                                                         liquidityProfile.accountNumber(),
                                                                                         liquidityProfile.currency(),
                                                                                         liquidityProfile.isActive()));

                }
            }
        }

        return new ModifyExistingParticipant.Output(output.modified(), output.participantId());
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
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        return switch (principalData.userRoleType()) {
            case OPERATION, ADMIN -> true;
            case SUPERUSER, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(ModifyExistingParticipant.Input input, ModifyExistingParticipant.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingParticipant.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
