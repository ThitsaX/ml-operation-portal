package com.thitsaworks.dfsp_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.audit.domain.Auditor;
import com.thitsaworks.dfsp_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.security.SecurityContext;
import com.thitsaworks.dfsp_portal.component.usecase.UseCaseContext;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.hubuser.domain.command.CreateAnnouncement;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.CreateNewAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateNewAnnouncementBean extends CreateNewAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementBean.class);

    @Autowired
    private CreateAnnouncement createAnnouncement;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @WriteTransactional
    public CreateNewAnnouncement.Output onExecute(CreateNewAnnouncement.Input input) throws Exception {

        CreateAnnouncement.Output output =
                this.createAnnouncement.execute(
                        new CreateAnnouncement.Input(input.getAnnouncementTitle(), input.getAnnouncementDetail(),input.getAnnouncementDate()));

        return new CreateNewAnnouncement.Output(output.isCreated());
    }

    @Override
    protected String getName() {

        return CreateNewAnnouncement.class.getCanonicalName();
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

        return CreateNewAnnouncement.class.getName();
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
    public void onAudit(CreateNewAnnouncement.Input input, CreateNewAnnouncement.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, CreateNewAnnouncement.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
