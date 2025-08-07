package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.WindowInfoData;
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
public class GetNetTransferAmountByWindowIdHandler
    extends OperationPortalAuditableUseCase<GetNetTransferAmountByWindowId.Input, GetNetTransferAmountByWindowId.Output>
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

        GetNetTransferAmountByWindowIdQuery.Output
            output =
            this.getNetTrasferAmountByWindowIdQuery.execute(new GetNetTransferAmountByWindowIdQuery.Input(
                input.settlementWindowId()));

        List<GetNetTransferAmountByWindowId.Detail> details = new ArrayList<>();

        for (WindowInfoData windowInfo : output.getWindowInfoList()) {

            GetNetTransferAmountByWindowId.Detail detail = new GetNetTransferAmountByWindowId.Detail(
                windowInfo.getDfspName(),
                windowInfo.getDebit(),
                windowInfo.getCredit(),
                windowInfo.getCurrencyId());

            details.add(detail);
        }

        String windowOpenedDate = output.getWindowInfoList().isEmpty() ? null :
            output.getWindowInfoList().get(0).getWindowOpenedDate();

        String windowClosedDate = output.getWindowInfoList().isEmpty() ? null :
            output.getWindowInfoList().get(0).getWindowClosedDate();

        return new GetNetTransferAmountByWindowId.Output(
            input.settlementWindowId(),
            windowOpenedDate,
            windowClosedDate,
            details);

    }

}
