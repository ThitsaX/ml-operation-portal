package com.thitsaworks.operation_portal.core.settlement.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.settlement.command.CreateSettlementModelCommand;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.model.SettlementModel;
import com.thitsaworks.operation_portal.core.settlement.repository.SettlementModelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateSettlementModelCommandHandler implements CreateSettlementModelCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementModelCommandHandler.class);

    private final SettlementModelRepository settlementModelRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws SettlementException {

        Optional<SettlementModel> optionalSettlementModel = this.settlementModelRepository.findOne(
                SettlementModelRepository.Filters.findByName(input.name()));

        if (optionalSettlementModel.isPresent()) {
            throw new SettlementException(SettlementErrors.SETTLEMENT_MODEL_ALREADY_REGISTERED.format(input.name()));
        }

        SettlementModel settlementModel = new SettlementModel(input.name(),
                                                              input.currencyId(),
                                                              input.isActive(),
                                                              input.autoCloseWindow(),
                                                              input.requireLiquidityCheck(),
                                                              input.autoPositionReset(),
                                                              input.adjustPosition());

        if (input.autoCloseWindow() && !input.schedulerConfigIdList().isEmpty()) {

            for (SchedulerConfigId schedulerConfigId : input.schedulerConfigIdList()) {
                settlementModel.addSchedulerConfig(schedulerConfigId);
            }

        }

        this.settlementModelRepository.save(settlementModel);

        return new Output(true, settlementModel.getSettlementModelId());

    }

}
