package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetParticipantListHandler
    extends OperationPortalUseCase<GetParticipantList.Input, GetParticipantList.Output>
    implements GetParticipantList {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantListHandler.class);

    private final ParticipantQuery participantQuery;

    public GetParticipantListHandler(PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     ParticipantQuery participantQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
    }

    @Override
    public GetParticipantList.Output onExecute(GetParticipantList.Input input) throws DomainException {

        List<ParticipantData> participantDataList = this.participantQuery.getParticipants();

        List<GetParticipantList.Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {

            if (participantData.participantName() != null &&
                    !participantData.participantName().getValue().toLowerCase().contains("hub")) {
                
                participantInfoList.add(new GetParticipantList.Output.ParticipantInfo(participantData.participantId(),
                                                                                      participantData.participantName()
                                                                                                     .getValue(),
                                                                                      participantData.description(),
                                                                                      participantData.address(),
                                                                                      participantData.mobile(),
                                                                                      participantData.logoDataType(),
                                                                                      participantData.logo(),
                                                                                      Instant.ofEpochSecond(
                                                                                              participantData.createdDate())));
            }
        }

        return new GetParticipantList.Output(participantInfoList);
    }

}
