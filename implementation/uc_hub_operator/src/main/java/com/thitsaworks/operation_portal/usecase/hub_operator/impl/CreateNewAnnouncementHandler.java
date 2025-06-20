package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateAnnouncement;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewAnnouncement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewAnnouncementHandler extends CreateNewAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementHandler.class);

    private final CreateAnnouncement createAnnouncement;

    private final ObjectMapper objectMapper;

    @Override
    public CreateNewAnnouncement.Output onExecute(CreateNewAnnouncement.Input input) throws Exception {

        CreateAnnouncement.Output output =
                this.createAnnouncement.execute(
                        new CreateAnnouncement.Input(input.announcementTitle(),
                                                     input.announcementDetail(),
                                                     input.announcementDate()));

        return new CreateNewAnnouncement.Output(output.created());
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
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
