package com.thitsaworks.operation_portal.core.settlement.query;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;

import java.util.List;
import java.util.Optional;

public interface SettlementModelQuery {

    List<SettlementModelData> getSettlementModels();

    SettlementModelData get(SettlementModelId settlementModelId) throws SettlementException;

    Optional<SettlementModelData> get(String settlementModelId);

    SettlementModelData get(SchedulerConfigId schedulerConfigId) throws SettlementException;

}

