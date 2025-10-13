package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigStatusCommand;
import com.thitsaworks.operation_portal.core.settlement.command.ModifySettlementModelCommand;
import com.thitsaworks.operation_portal.core.settlement.command.impl.ModifySettlementModelCommandHandler;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySettlementModel;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifySettlementModelHandler
        extends OperationPortalAuditableUseCase<ModifySettlementModel.Input, ModifySettlementModel.Output>
        implements ModifySettlementModel {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySettlementModelHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final ModifySettlementModelCommand modifySettlementModelCommand;

    private final ModifySchedulerConfigStatusCommand modifySchedulerConfigStatusCommand;

    private final ObjectMapper objectMapper;

    public ModifySettlementModelHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SettlementModelQuery settlementModelQuery,
                                        ModifySettlementModelCommand modifySettlementModelCommand,
                                        ModifySchedulerConfigStatusCommand modifySchedulerConfigStatusCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementModelQuery = settlementModelQuery;
        this.modifySettlementModelCommand = modifySettlementModelCommand;
        this.modifySchedulerConfigStatusCommand = modifySchedulerConfigStatusCommand;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        //Activate or Deactivate all schedulers when Auto-Manual changes occurs.
        if (input.autoCloseWindow() != settlementModelData.autoCloseWindow()) {

            for (SchedulerConfigId schedulerConfigId : settlementModelData.schedulerConfigIds()) {
                this.modifySchedulerConfigStatusCommand.execute(new ModifySchedulerConfigStatusCommand.Input(
                        schedulerConfigId,
                        input.autoCloseWindow()));
            }

        }

        var output = this.modifySettlementModelCommand.execute(new ModifySettlementModelCommandHandler.Input(
                settlementModelData.settlementModelId(),
                input.settlementModelName(),
                input.currencyID(),
                input.isActive(),
                input.autoCloseWindow()));

        return new Output(output.modified(), output.settlementModelId());

    }

}
