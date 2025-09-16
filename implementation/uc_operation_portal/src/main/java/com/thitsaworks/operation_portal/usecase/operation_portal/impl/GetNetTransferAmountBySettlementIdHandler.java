package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountBySettlementIdQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountBySettlementId;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetNetTransferAmountBySettlementIdHandler
    extends OperationPortalAuditableUseCase<GetNetTransferAmountBySettlementId.Input, GetNetTransferAmountBySettlementId.Output>
    implements GetNetTransferAmountBySettlementId {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountBySettlementIdHandler.class);

    private final GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery;

    public GetNetTransferAmountBySettlementIdHandler(CreateInputAuditCommand createInputAuditCommand,
                                                     CreateOutputAuditCommand createOutputAuditCommand,
                                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                                     ObjectMapper objectMapper,
                                                     PrincipalCache principalCache,
                                                     ActionAuthorizationManager actionAuthorizationManager,
                                                     GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.getNetTransferAmountBySettlementIdQuery = getNetTransferAmountBySettlementIdQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetNetTransferAmountBySettlementIdQuery.Output
            output =
            this.getNetTransferAmountBySettlementIdQuery.execute(new GetNetTransferAmountBySettlementIdQuery.Input(
                input.settlementId()));

        List<Detail> details = new ArrayList<>();

        for (SettlementWindowInfoData windowInfo : output.getWindowInfoList()) {

            Detail detail = new Detail(
                windowInfo.getDfspName(),
                windowInfo.getDebit(),
                windowInfo.getCredit(),
                windowInfo.getCurrencyId());

            details.add(detail);
        }

        String
            windowOpenedDate =
            output.getWindowInfoList()
                  .isEmpty() ? null :
                output.getWindowInfoList()
                      .get(0)
                      .getWindowOpenedDate();

        String
            windowClosedDate =
            output.getWindowInfoList()
                  .isEmpty() ? null :
                output.getWindowInfoList()
                      .get(0)
                      .getWindowClosedDate();

        String
            settlementWindowIds =
            output.getWindowInfoList()
                  .isEmpty() ? null :
                output.getWindowInfoList()
                      .get(0)
                      .getSettlementWindowIds();

        return new Output(
            input.settlementId(),
            settlementWindowIds,
            windowOpenedDate,
            windowClosedDate,
            details);

    }

}
