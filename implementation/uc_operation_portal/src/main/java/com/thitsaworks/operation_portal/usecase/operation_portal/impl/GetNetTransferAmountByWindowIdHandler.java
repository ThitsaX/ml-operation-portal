package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountByWindowIdQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountByWindowId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetNetTransferAmountByWindowIdHandler extends OperationPortalAuditableUseCase<GetNetTransferAmountByWindowId.Input, GetNetTransferAmountByWindowId.Output>
    implements GetNetTransferAmountByWindowId {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountByWindowIdHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetNetTransferAmountByWindowIdQuery getNetTrasferAmountByWindowIdQuery;


    public GetNetTransferAmountByWindowIdHandler(CreateInputAuditCommand createInputAuditCommand,
                                                 CreateOutputAuditCommand createOutputAuditCommand,
                                                 CreateExceptionAuditCommand createExceptionAuditCommand,
                                                 ObjectMapper objectMapper,
                                                 PrincipalCache principalCache,
                                                 GetNetTransferAmountByWindowIdQuery getNetTransferAmountByWindowIdQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.getNetTrasferAmountByWindowIdQuery = getNetTransferAmountByWindowIdQuery;


    }

    @Override
    protected Output onExecute(Input input) throws DomainException {


    //    GetNetTransferAmountByWindowId.Output output = this.getNetTransferAmountByWindowIdQuery.execute(new GetNetTransferAmountByWindowId.Input());

        GetNetTransferAmountByWindowId.Output output = null;
        List<TransferData> transferDataList = new ArrayList<>();



        return new Output(0,null,null,null);

    }

}
