package com.thitsaworks.operation_portal.component.common.identifier;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SettlementSchedulerConfigId implements Serializable {

    private SettlementModelId settlementModelId;

    @Column(name = "scheduler_config_id")
    private Long schedulerConfigId;

}
