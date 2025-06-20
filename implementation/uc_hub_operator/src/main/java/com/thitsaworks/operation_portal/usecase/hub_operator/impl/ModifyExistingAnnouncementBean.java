package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyAnnouncement;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingAnnouncement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ModifyExistingAnnouncementBean extends ModifyExistingAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingAnnouncementBean.class);

    private final ModifyAnnouncement modifyAnnouncement;

    private final ObjectMapper objectMapper;

    @Override
    public ModifyExistingAnnouncement.Output onExecute(ModifyExistingAnnouncement.Input input) throws Exception {

        ModifyAnnouncement.Output output = this.modifyAnnouncement.execute(
                new ModifyAnnouncement.Input(input.announcementId(), input.announcementTitle(),
                                             input.announcementDetail(), input.announcementDate(), input.isDeleted()));

        return new ModifyExistingAnnouncement.Output(output.announcementId(), output.modified());
    }

    @Override
    protected String getName() {

        return ModifyExistingAnnouncement.class.getCanonicalName();
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

        return ModifyExistingAnnouncement.class.getName();
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
    public void onAudit(ModifyExistingAnnouncement.Input input, ModifyExistingAnnouncement.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, ModifyExistingAnnouncement.class, input, output,
                      new UserId(securityContext.userId()),
                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
    }

}
