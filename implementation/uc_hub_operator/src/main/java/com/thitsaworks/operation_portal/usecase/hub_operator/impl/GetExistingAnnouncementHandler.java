package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetExistingAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetExistingAnnouncementHandler
        extends HubOperatorAuditableUseCase<GetExistingAnnouncement.Input, GetExistingAnnouncement.Output>
        implements GetExistingAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingAnnouncementHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final AnnouncementQuery announcementQuery;

    private final ObjectMapper objectMapper;

    @Autowired
    public GetExistingAnnouncementHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          AnnouncementQuery announcementQuery,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.announcementQuery = announcementQuery;
        this.objectMapper = objectMapper;
    }

    @Override
    public GetExistingAnnouncement.Output onExecute(GetExistingAnnouncement.Input input) throws
                                                                                         OperationPortalException {

        AnnouncementData announcementData = this.announcementQuery.get(input.announcementId());

        return new GetExistingAnnouncement.Output(announcementData.announcementId(),
                                                  announcementData.announcementTitle(),
                                                  announcementData.announcementDetail(),
                                                  announcementData.announcementDate(),
                                                  announcementData.isDeleted(),
                                                  announcementData.createdDate());

    }

}
