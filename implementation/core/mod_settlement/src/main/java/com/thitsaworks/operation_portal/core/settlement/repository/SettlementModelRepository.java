package com.thitsaworks.operation_portal.core.settlement.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.core.settlement.model.QSettlementModel;
import com.thitsaworks.operation_portal.core.settlement.model.SettlementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementModelRepository
        extends JpaRepository<SettlementModel, SettlementModelId>, QuerydslPredicateExecutor<SettlementModel> {

    class Filters {

        private static final QSettlementModel settlementModel = QSettlementModel.settlementModel;

        public static BooleanExpression findByName(String name) {

            return settlementModel.name.eq(name);
        }

        public static BooleanExpression findBySchedulerConfigId(SchedulerConfigId schedulerConfigId) {

            return settlementModel.settlementSchedulerConfigs.any().settlementSchedulerConfigId.schedulerConfigId.eq(
                    schedulerConfigId.getId());
        }

    }

}
