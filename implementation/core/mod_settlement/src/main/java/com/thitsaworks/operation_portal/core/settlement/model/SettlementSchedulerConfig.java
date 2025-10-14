package com.thitsaworks.operation_portal.core.settlement.model;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementSchedulerConfigId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_settlement_scheduler_config")
@NoArgsConstructor
@Getter
public class SettlementSchedulerConfig {

    @EmbeddedId
    protected SettlementSchedulerConfigId settlementSchedulerConfigId;

    @ManyToOne()
    @MapsId("settlementModelId")
    @JoinColumn(name = "settlement_model_id")
    protected SettlementModel settlementModel;

    public SettlementSchedulerConfig(SettlementModel settlementModel,
                                     SchedulerConfigId schedulerConfigId) {

        this.settlementSchedulerConfigId = new SettlementSchedulerConfigId(settlementModel.getSettlementModelId(),
                                                                           schedulerConfigId.getId());
        this.settlementModel = settlementModel;
    }

}


