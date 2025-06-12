package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.hubuser.domain.command.ModifyAnnouncement;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyExistingAnnouncementBean extends ModifyExistingAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingAnnouncementBean.class);

    @Autowired
    private ModifyAnnouncement modifyAnnouncement;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public ModifyExistingAnnouncement.Output onExecute(ModifyExistingAnnouncement.Input input) throws Exception {

        ModifyAnnouncement.Output output = this.modifyAnnouncement.execute(
                new ModifyAnnouncement.Input(input.getAnnouncementId(), input.getAnnouncementTitle(),
                        input.getAnnouncementDetail(),input.getAnnouncementDate(), input.isDeleted()));

        return new ModifyExistingAnnouncement.Output(output.getAnnouncementId(), output.isModified());
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
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
