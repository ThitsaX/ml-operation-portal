package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllParticipantHandler extends GetAllParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantHandler.class);

    private final ParticipantQuery participantQuery;

    private final ObjectMapper objectMapper;

    @Override
    public GetAllParticipant.Output onExecute(GetAllParticipant.Input input) throws Exception {

        List<ParticipantData> participantDataList = this.participantQuery.getParticipants();

        List<GetAllParticipant.Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {

            participantInfoList.add(
                    new GetAllParticipant.Output.ParticipantInfo(participantData.participantId(),
                            participantData.dfspCode().getValue(),
                            participantData.name(),
                            participantData.dfspName(),
                            participantData.address(),
                            participantData.mobile(),
                            participantData.businessContactId() == null ? null :
                                    participantData.businessContactId().toString(),
                            participantData.technicalContactId() == null ? null :
                                    participantData.technicalContactId().toString(),
                            Instant.ofEpochSecond(participantData.createdDate())));
        }

        return new GetAllParticipant.Output(participantInfoList);
    }

    @Override
    protected String getName() {

        return GetAllParticipant.class.getCanonicalName();
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

        return GetAllParticipant.class.getName();
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
    public void onAudit(GetAllParticipant.Input input, GetAllParticipant.Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetAllParticipant.class, input, output,
                new UserId(securityContext.userId()),
                securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
