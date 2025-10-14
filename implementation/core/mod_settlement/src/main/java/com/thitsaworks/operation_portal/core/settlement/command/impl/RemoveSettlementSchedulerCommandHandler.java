package com.thitsaworks.operation_portal.core.settlement.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.settlement.command.RemoveSettlementSchedulerCommand;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.model.SettlementModel;
import com.thitsaworks.operation_portal.core.settlement.repository.SettlementModelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveSettlementSchedulerCommandHandler implements RemoveSettlementSchedulerCommand {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSettlementSchedulerCommandHandler.class);

    private final SettlementModelRepository settlementModelRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws SettlementException {

        SettlementModel settlementModel =
                this.settlementModelRepository.findById(input.settlementModelId())
                                              .orElseThrow(() -> new SettlementException(SettlementErrors.SETTLEMENT_MODEL_NOT_FOUND.format(
                                                      input.settlementModelId().getId().toString())));

        settlementModel.removeSchedulerConfig(input.schedulerConfigId());

        this.settlementModelRepository.save(settlementModel);

        return new Output(true, input.schedulerConfigId());

    }

}
