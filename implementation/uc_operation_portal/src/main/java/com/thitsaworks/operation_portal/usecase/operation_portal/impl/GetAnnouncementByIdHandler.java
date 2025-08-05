package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAnnouncementById;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAnnouncementByIdHandler
    extends OperationPortalAuditableUseCase<GetAnnouncementById.Input, GetAnnouncementById.Output>
    implements GetAnnouncementById {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementByIdHandler.class);

    private final AnnouncementQuery announcementQuery;

    @Autowired
    public GetAnnouncementByIdHandler(CreateInputAuditCommand createInputAuditCommand,
                                      CreateOutputAuditCommand createOutputAuditCommand,
                                      CreateExceptionAuditCommand createExceptionAuditCommand,
                                      AnnouncementQuery announcementQuery,
                                      ObjectMapper objectMapper,
                                      PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

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
