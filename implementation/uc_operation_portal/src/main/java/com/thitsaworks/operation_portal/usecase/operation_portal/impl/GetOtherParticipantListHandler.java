package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetOtherParticipantList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetOtherParticipantListHandler
    extends OperationPortalUseCase<GetOtherParticipantList.Input, GetOtherParticipantList.Output>
    implements GetOtherParticipantList {

    private static final Logger LOG = LoggerFactory.getLogger(GetOtherParticipantListHandler.class);

    private final ParticipantQuery participantQuery;

    public GetOtherParticipantListHandler(PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          ParticipantQuery participantQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<ParticipantData> participantDataList =
            this.participantQuery.getOtherParticipants(input.participantId());

        List<ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {
            participantInfoList.add(new ParticipantInfo(participantData.participantId(),
                                                        participantData.participantName(),
                                                        participantData.description()));
        }

        return new Output(participantInfoList);
    }

}
