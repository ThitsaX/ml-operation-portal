package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.operation_portal.participant.query.GetParticipants;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllParticipantBean extends GetAllParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantBean.class);

    @Autowired
    private GetParticipants getParticipants;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @WriteTransactional
    public GetAllParticipant.Output onExecute(GetAllParticipant.Input input) throws Exception {

        GetParticipants.Output output = this.getParticipants.execute(new GetParticipants.Input());

        List<GetAllParticipant.Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (GetParticipants.Output.ParticipantInfo data : output.getParticipantInfoList()) {

            participantInfoList.add(
                    new GetAllParticipant.Output.ParticipantInfo(data.getParticipantId(), data.getDfsp_code(),
                            data.getName(), data.getDfsp_name(),data.getAddress(), data.getMobile(), data.getBusinessContact(),
                            data.getTechnicalContact(), data.getCreatedDate()));
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
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
