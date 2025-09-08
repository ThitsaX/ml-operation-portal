package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferDetailQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferDetail;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetTransferDetailHandler
    extends OperationPortalAuditableUseCase<GetTransferDetail.Input, GetTransferDetail.Output>
    implements GetTransferDetail {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailHandler.class);

    private final GetTransferDetailQuery getTransferDetailQuery;

    public GetTransferDetailHandler(CreateInputAuditCommand createInputAuditCommand,
                                    CreateOutputAuditCommand createOutputAuditCommand,
                                    CreateExceptionAuditCommand createExceptionAuditCommand,
                                    ObjectMapper objectMapper,
                                    PrincipalCache principalCache,
                                    ActionAuthorizationManager actionAuthorizationManager,
                                    GetTransferDetailQuery getTransferDetailQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.getTransferDetailQuery = getTransferDetailQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        TransferDetailData transferDetailData = this.getTransferDetailQuery.execute(input.transferId());

        return new Output(transferDetailData);
    }

}
