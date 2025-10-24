package com.thitsaworks.operation_portal.core.settlement.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.settlement.command.ModifySettlementModelCommand;
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
public class ModifySettlementModelCommandHandler implements ModifySettlementModelCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySettlementModelCommandHandler.class);

    private final SettlementModelRepository settlementModelRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws SettlementException {

        SettlementModel settlementModel =
                this.settlementModelRepository.findById(input.settlementModelId())
                                              .orElseThrow(() -> new SettlementException(SettlementErrors.SETTLEMENT_MODEL_NOT_FOUND.format(
                                                      input.settlementModelId().getId().toString())));

        if (!input.name().equals(settlementModel.getName())) {
            Optional<SettlementModel> optionalSettlementModel = this.settlementModelRepository.findOne(
                    SettlementModelRepository.Filters.findByName(input.name()));

            if (optionalSettlementModel.isPresent()) {
                throw new SettlementException(SettlementErrors.SETTLEMENT_MODEL_ALREADY_REGISTERED.format(input.name()));
            }
        }

        settlementModel.name(input.name())
                       .type(input.type())
                       .currencyId(input.currencyId())
                       .isActive(input.isActive())
                       .autoCloseWindow(input.autoCloseWindow())
                       .manualCloseWindow(input.manualCloseWindow())
                       .zoneId(input.zoneId());

        this.settlementModelRepository.save(settlementModel);

        return new Output(true, settlementModel.getSettlementModelId());

    }

}
