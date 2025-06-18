package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ContactData;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.query.ContactQuery;
import com.thitsaworks.operation_portal.core.participant.query.LiquidityProfileQuery;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.common.GetExistingParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetExistingParticipantBean extends GetExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantBean.class);

    private final ParticipantQuery participantQuery;

    private final ContactQuery contactQuery;

    private final LiquidityProfileQuery liquidityProfileQuery;

    private final ObjectMapper objectMapper;

    private final PrincipalCache principalCache;

    @Override
    public GetExistingParticipant.Output onExecute(GetExistingParticipant.Input input) throws
                                                                                       ParticipantNotFoundException {

        ParticipantData participantData = this.participantQuery.get(input.participantId());

        List<ContactData> contactDataList = this.contactQuery.getContacts(input.participantId());

        List<Output.ContactInfo> contactInfoList = new ArrayList<>();

        for (ContactData contactData : contactDataList) {

            contactInfoList.add(
                    new Output.ContactInfo(contactData.contactId(),
                                           contactData.name(),
                                           contactData.title(),
                                           contactData.email(),
                                           contactData.mobile(),
                                           contactData.contactType().name()));
        }

        List<LiquidityProfileData> liquidityProfileDataList =
                this.liquidityProfileQuery.getLiquidityProfiles(input.participantId());

        List<Output.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (LiquidityProfileData liquidityProfileData : liquidityProfileDataList) {

            liquidityProfileInfoList.add(
                    new Output.LiquidityProfileInfo(liquidityProfileData.liquidityProfileId(),
                                                    liquidityProfileData.accountName(),
                                                    liquidityProfileData.accountNumber(),
                                                    liquidityProfileData.currency(),
                                                    liquidityProfileData.isActive()));
        }

        GetExistingParticipant.Output result =
                new GetExistingParticipant.Output(participantData.participantId(),
                                                  participantData.dfspCode(),
                                                  participantData.name(),
                                                  participantData.address(),
                                                  participantData.mobile(),
                                                  participantData.createdDate(),
                        contactInfoList,
                        liquidityProfileInfoList);

        return result;
    }

    @Override
    protected String getName() {

        return GetExistingParticipant.class.getCanonicalName();
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

        return GetExistingParticipant.class.getName();
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
    public void onAudit(GetExistingParticipant.Input input, GetExistingParticipant.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetExistingParticipant.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
