package com.thitsaworks.operation_portal.core.settlement.model;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_settlement_model")
@NoArgsConstructor
@Getter
public class SettlementModel extends JpaEntity<SettlementModelId> {

    @EmbeddedId
    protected SettlementModelId settlementModelId;

    @Column(name = "name")
    protected String name;

    @Column(name = "type")
    protected String type;

    @Column(name = "currency_id")
    protected String currencyId;

    @Column(name = "is_active")
    protected boolean isActive;

    @Column(name = "auto_close_window")
    protected boolean autoCloseWindow;

    @Column(name = "manual_close_window")
    protected boolean manualCloseWindow;

    @Column(name = "zone_id")
    protected String zoneId;

    @Column(name = "require_liquidity_check")
    protected boolean requireLiquidityCheck;

    @Column(name = "auto_position_reset")
    protected boolean autoPositionReset;

    @Column(name = "adjust_position")
    protected boolean adjustPosition;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "settlementModel", orphanRemoval = true, fetch = FetchType.LAZY)
    Set<SettlementSchedulerConfig> settlementSchedulerConfigs = new HashSet<>();

    public SettlementModel(String name,
                           String type,
                           String currencyId,
                           boolean isActive,
                           boolean autoCloseWindow,
                           boolean manualCloseWindow,
                           String zoneId,
                           boolean requireLiquidityCheck,
                           boolean autoPositionReset,
                           boolean adjustPosition) {

        assert name != null : "name is required!";

        this.settlementModelId = new SettlementModelId(Snowflake.get().nextId());
        this.name = name;
        this.type = type;
        this.currencyId = currencyId;
        this.isActive = isActive;
        this.autoCloseWindow = autoCloseWindow;
        this.manualCloseWindow = manualCloseWindow;
        this.zoneId = zoneId;
        this.requireLiquidityCheck = requireLiquidityCheck;
        this.autoPositionReset = autoPositionReset;
        this.adjustPosition = adjustPosition;
    }

    public void addSchedulerConfig(SchedulerConfigId schedulerConfigId) throws SettlementException {

        boolean isDuplicated = this.settlementSchedulerConfigs.stream().anyMatch(schedulerConfig ->
                                                                                         schedulerConfig.getSettlementSchedulerConfigId()
                                                                                                        .getSchedulerConfigId()
                                                                                                        .equals(schedulerConfigId.getId())
                                                                                );

        if (isDuplicated) {
            throw new SettlementException(SettlementErrors.SCHEDULER_ALREADY_ADDED.format(schedulerConfigId.getId()));
        }

        this.settlementSchedulerConfigs.add(new SettlementSchedulerConfig(this, schedulerConfigId));
    }

    public void removeSchedulerConfig(SchedulerConfigId schedulerConfigId) throws SettlementException {

        this.settlementSchedulerConfigs.removeIf(schedulerConfig -> schedulerConfig.getSettlementSchedulerConfigId()
                                                                                   .getSchedulerConfigId()
                                                                                   .equals(schedulerConfigId.getId()));
    }

    @Override
    public SettlementModelId getId() {

        return this.settlementModelId;
    }

    public SettlementModel name(String name) {

        this.name = name;

        return this;
    }

    public SettlementModel type(String type) {

        this.type = type;

        return this;
    }

    public SettlementModel currencyId(String currencyId) {

        this.currencyId = currencyId;

        return this;
    }

    public SettlementModel isActive(boolean isActive) {

        this.isActive = isActive;

        return this;
    }

    public SettlementModel autoCloseWindow(boolean autoCloseWindow) {

        this.autoCloseWindow = autoCloseWindow;

        return this;
    }

    public SettlementModel manualCloseWindow(boolean manualCloseWindow) {

        this.manualCloseWindow = manualCloseWindow;

        return this;
    }

    public SettlementModel zoneId(String zoneId) {

        this.zoneId = zoneId;

        return this;
    }

    public SettlementModel requireLiquidityCheck(boolean requireLiquidityCheck) {

        this.requireLiquidityCheck = requireLiquidityCheck;

        return this;
    }

    public SettlementModel autoPositionReset(boolean autoPositionReset) {

        this.autoPositionReset = autoPositionReset;

        return this;
    }

    public SettlementModel adjustPosition(boolean adjustPosition) {

        this.adjustPosition = adjustPosition;

        return this;
    }

}


