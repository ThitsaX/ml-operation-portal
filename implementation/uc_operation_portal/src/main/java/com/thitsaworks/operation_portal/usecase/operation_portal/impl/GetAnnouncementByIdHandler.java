package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.participant.query.AnnouncementQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAnnouncementById;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAnnouncementByIdHandler
    extends OperationPortalUseCase<GetAnnouncementById.Input, GetAnnouncementById.Output>
    implements GetAnnouncementById {

    private static final Logger LOG = LoggerFactory.getLogger(GetAnnouncementByIdHandler.class);

    private final AnnouncementQuery announcementQuery;

    @Autowired
    public GetAnnouncementByIdHandler(AnnouncementQuery announcementQuery,
                                      PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager) {

        super(principalCache,
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
