package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ContactData;
import com.thitsaworks.operation_portal.core.participant.data.LiquidityProfileData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ContactQuery;
import com.thitsaworks.operation_portal.core.participant.query.LiquidityProfileQuery;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetExistingParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetExistingParticipantHandler
    extends OperationPortalAuditableUseCase<GetExistingParticipant.Input, GetExistingParticipant.Output>
    implements GetExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ParticipantQuery participantQuery;

    private final ContactQuery contactQuery;

    private final LiquidityProfileQuery liquidityProfileQuery;

    public GetExistingParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         ParticipantQuery participantQuery,
                                         ContactQuery contactQuery,
                                         LiquidityProfileQuery liquidityProfileQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.participantQuery = participantQuery;
        this.contactQuery = contactQuery;
        this.liquidityProfileQuery = liquidityProfileQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ParticipantData participantData = this.participantQuery.get(input.participantId());

        List<ContactData> contactDataList = this.contactQuery.getContacts(input.participantId());

        List<Output.ContactInfo> contactInfoList = new ArrayList<>();

        for (ContactData contactData : contactDataList) {

            contactInfoList.add(
                new Output.ContactInfo(contactData.contactId(),
                                       contactData.name(),
                                       contactData.position(),
                                       contactData.email(),
                                       contactData.mobile(),
                                       contactData.contactType()
                                                  .name()));
        }

        List<LiquidityProfileData> liquidityProfileDataList =
            this.liquidityProfileQuery.getLiquidityProfiles(input.participantId());

        List<Output.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (LiquidityProfileData liquidityProfileData : liquidityProfileDataList) {

            liquidityProfileInfoList.add(
                new Output.LiquidityProfileInfo(liquidityProfileData.liquidityProfileId(),
                                                liquidityProfileData.bankName(),
                                                liquidityProfileData.accountName(),
                                                liquidityProfileData.accountNumber(),
                                                liquidityProfileData.currency(),
                                                liquidityProfileData.isActive()));
        }

        return new Output(participantData.participantId(),
                          participantData.participantName()
                                         .getValue(),
                          participantData.description(),
                          participantData.address(),
                          participantData.mobile(),
                          participantData.logoDataType(),
                          participantData.logo(),
                          Instant.ofEpochSecond(participantData.createdDate()),
                          contactInfoList,
                          liquidityProfileInfoList);
    }

}
