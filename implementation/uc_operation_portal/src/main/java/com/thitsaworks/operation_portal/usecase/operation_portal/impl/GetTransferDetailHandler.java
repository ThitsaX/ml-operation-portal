package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferDetailQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferDetail;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetTransferDetailHandler
    extends OperationPortalUseCase<GetTransferDetail.Input, GetTransferDetail.Output>
    implements GetTransferDetail {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailHandler.class);

    private final GetTransferDetailQuery getTransferDetailQuery;

    public GetTransferDetailHandler(PrincipalCache principalCache,
                                    ActionAuthorizationManager actionAuthorizationManager,
                                    GetTransferDetailQuery getTransferDetailQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.getTransferDetailQuery = getTransferDetailQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        TransferDetailData transferDetailData = this.getTransferDetailQuery.execute(input.transferId());

        return new Output(transferDetailData);
    }

}
