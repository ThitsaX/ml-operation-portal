package com.thitsaworks.dfsp_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.participant.query.GetOtherParticipants;
import com.thitsaworks.dfsp_portal.participant.query.GetParticipants;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.GetAllParticipant;
import com.thitsaworks.dfsp_portal.usecase.participant.GetAllOtherParticipants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllOtherParticipantsBean extends GetAllOtherParticipants {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsBean.class);

    @Autowired
    private GetOtherParticipants getOtherParticipants;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @WriteTransactional
    public Output onExecute(Input input) throws Exception {

        GetOtherParticipants.Output output = this.getOtherParticipants.execute(new GetOtherParticipants.Input(input.getParticipantId()));

        List<Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (GetOtherParticipants.Output.ParticipantInfo data : output.getParticipantInfoList()) {

            participantInfoList.add(
                    new Output.ParticipantInfo(data.getParticipantId(), data.getDfsp_code(),
                            data.getName()));
        }

        return new Output(participantInfoList);
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



}
