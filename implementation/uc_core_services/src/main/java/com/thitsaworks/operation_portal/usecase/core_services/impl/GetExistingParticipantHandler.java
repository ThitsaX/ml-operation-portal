package com.thitsaworks.operation_portal.usecase.core_services.impl;

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
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.GetExistingParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetExistingParticipantHandler
    extends CoreServicesAuditableUseCase<GetExistingParticipant.Input, GetExistingParticipant.Output>
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
                                       contactData.title(),
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
                                                liquidityProfileData.accountName(),
                                                liquidityProfileData.accountNumber(),
                                                liquidityProfileData.currency(),
                                                liquidityProfileData.isActive()));
        }

        GetExistingParticipant.Output result =
            new GetExistingParticipant.Output(participantData.participantId(),
                                              participantData.dfspCode()
                                                             .getValue(),
                                              participantData.name(),
                                              participantData.address(),
                                              participantData.mobile(),
                                              Instant.ofEpochSecond(participantData.createdDate()),
                                              contactInfoList,
                                              liquidityProfileInfoList);

        return result;
    }

}
