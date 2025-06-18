package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.core.participant.query.GetOtherParticipantsQuery;
import com.thitsaworks.operation_portal.usecase.participant.GetAllOtherParticipants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllOtherParticipantsBean extends GetAllOtherParticipants {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsBean.class);

    @Autowired
    private GetOtherParticipantsQuery getOtherParticipantsQuery;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws Exception {

        List<ParticipantInfo> participantInfoList =
                this.getOtherParticipantsQuery.getOtherParticipants(input.getParticipantId());


        return new Output(participantInfoList);
    }

    @Override
    protected String getName() {

        return GetAllOtherParticipants.class.getCanonicalName();
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

        return GetAllOtherParticipants.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }



}
