package com.thitsaworks.operation_portal.core.settlement.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementSchedulerConfigId;
import com.thitsaworks.operation_portal.core.settlement.model.SettlementModel;
import com.thitsaworks.operation_portal.core.settlement.model.SettlementSchedulerConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementSchedulerConfigRepository
        extends JpaRepository<SettlementSchedulerConfig, SettlementSchedulerConfigId>, QuerydslPredicateExecutor<SettlementSchedulerConfig> {

}
