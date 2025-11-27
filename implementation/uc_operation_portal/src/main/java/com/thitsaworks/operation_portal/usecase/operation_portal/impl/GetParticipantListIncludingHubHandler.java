package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantListIncludingHub;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetParticipantListIncludingHubHandler
    extends OperationPortalUseCase<GetParticipantListIncludingHub.Input, GetParticipantListIncludingHub.Output>
    implements GetParticipantListIncludingHub {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantListIncludingHubHandler.class);

    private final ParticipantQuery participantQuery;

    public GetParticipantListIncludingHubHandler(PrincipalCache principalCache,
                                                 ActionAuthorizationManager actionAuthorizationManager,
                                                 ParticipantQuery participantQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        List<ParticipantData> participantDataList = this.participantQuery.getAllParticipants();

        List<Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {

            if (participantData.participantName() != null) {

                participantInfoList.add(new Output.ParticipantInfo(participantData.participantId(),
                                                                   participantData.participantName()
                                                                                  .getValue(),
                                                                   participantData.description(),
                                                                   participantData.address(),
                                                                   participantData.mobile(),
                                                                   participantData.logoFileType(),
                                                                   participantData.logo(),
                                                                   Instant.ofEpochSecond(
                                                                       participantData.createdDate())));
            }
        }

        return new Output(participantInfoList);
    }

}
