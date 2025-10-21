package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.settlement.command.CreateSettlementModelCommand;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlementModel;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CreateSettlementModelHandler
        extends OperationPortalAuditableUseCase<CreateSettlementModel.Input, CreateSettlementModel.Output>
        implements CreateSettlementModel {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementModelHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final CreateSettlementModelCommand createSettlementModelCommand;

    public CreateSettlementModelHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SettlementModelQuery settlementModelQuery,
                                        CreateSettlementModelCommand createSettlementModelCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementModelQuery = settlementModelQuery;
        this.createSettlementModelCommand = createSettlementModelCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        Optional<SettlementModelData> optionalSettlementModelData =
                this.settlementModelQuery.get(input.settlementModelName());

        if (optionalSettlementModelData.isPresent()) {

            throw new SettlementException(SettlementErrors.SETTLEMENT_MODEL_ALREADY_REGISTERED.format(input.settlementModelName()));

        }

        List<SchedulerConfigId> schedulerConfigIdList = new ArrayList<>();

        var output = this.createSettlementModelCommand.execute(new CreateSettlementModelCommand.Input(

                input.settlementModelName(),
                input.modelType(),
                input.currencyID(),
                true,
                false,
                input.requireLiquidityCheck(),
                input.autoPositionReset(),
                input.adjustPosition(),
                schedulerConfigIdList));

        return new Output(output.created(), output.settlementModelId());

    }

}
