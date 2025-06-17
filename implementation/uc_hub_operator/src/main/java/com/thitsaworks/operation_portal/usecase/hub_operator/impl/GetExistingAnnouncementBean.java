package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.hubuser.query.GetAnnouncement;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetExistingAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetExistingAnnouncementBean extends GetExistingAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingAnnouncementBean.class);

    @Autowired
    private GetAnnouncement getAnnouncement;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @DfspWriteTransactional
    public GetExistingAnnouncement.Output onExecute(GetExistingAnnouncement.Input input) throws Exception {

        GetAnnouncement.Output output =
                this.getAnnouncement.execute(new GetAnnouncement.Input(input.getAnnouncementId()));

        GetExistingAnnouncement.Output result =
                new GetExistingAnnouncement.Output(output.getAnnouncementId(), output.getAnnouncementTitle(),
                        output.getAnnouncementDetail(), output.getAnnouncementDate(), output.isDeleted(),
                        output.getCreatedDate());

        return result;
    }

    @Override
    protected String getName() {

        return GetExistingAnnouncement.class.getCanonicalName();
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

        return GetExistingAnnouncement.class.getName();
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
    public void onAudit(GetExistingAnnouncement.Input input, GetExistingAnnouncement.Output output)
            throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetExistingAnnouncement.class, input, output,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
