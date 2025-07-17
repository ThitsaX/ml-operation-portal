package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;

import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;

import com.thitsaworks.operation_portal.usecase.operation_portal.GetAnnouncementById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetAnnouncementByIdHandler
    extends OperationPortalAuditableUseCase<GetAnnouncementById.Input, GetAnnouncementById.Output>
    implements GetAnnouncementById {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementByIdHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final AnnouncementQuery announcementQuery;

    @Autowired
    public GetAnnouncementByIdHandler(CreateInputAuditCommand createInputAuditCommand,
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
    }

    @Override
    public GetAnnouncementById.Output onExecute(GetAnnouncementById.Input input) throws
                                                                                         DomainException {

        AnnouncementData announcementData = this.announcementQuery.get(input.announcementId());

        return new GetAnnouncementById.Output(announcementData.announcementId(),
                                                  announcementData.announcementTitle(),
                                                  announcementData.announcementDetail(),
                                                  announcementData.announcementDate(),
                                                  announcementData.isDeleted(),
                                                  announcementData.createdDate());

    }

}
