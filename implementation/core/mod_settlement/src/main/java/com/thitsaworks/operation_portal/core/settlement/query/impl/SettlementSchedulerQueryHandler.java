package com.thitsaworks.operation_portal.core.settlement.query.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.identifier.SettlementModelId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.model.QSchedulerConfig;
import com.thitsaworks.operation_portal.core.settlement.model.QSettlementSchedulerConfig;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementSchedulerQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class SettlementSchedulerQueryHandler implements SettlementSchedulerQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementSchedulerQueryHandler.class);

    private final QSettlementSchedulerConfig settlementSchedulerConfig =
            QSettlementSchedulerConfig.settlementSchedulerConfig;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SchedulerConfigData> getSettlementSchedulers(SettlementModelId settlementModelId) {

        QSchedulerConfig schedulerConfig = QSchedulerConfig.schedulerConfig;

        QSettlementSchedulerConfig settlementSchedulerConfig = QSettlementSchedulerConfig.settlementSchedulerConfig;

        List<Tuple> tupleSQLQuery = this.jpaQueryFactory.select(schedulerConfig.schedulerConfigId,
                                                                schedulerConfig.name,
                                                                schedulerConfig.jobName,
                                                                schedulerConfig.cronExpression,
                                                                schedulerConfig.description,
                                                                schedulerConfig.zoneId,
                                                                schedulerConfig.active)
                                                        .from(schedulerConfig)
                                                        .join(settlementSchedulerConfig)
                                                        .on(settlementSchedulerConfig.settlementSchedulerConfigId.schedulerConfigId.eq(
                                                                schedulerConfig.schedulerConfigId.id))
                                                        .where(settlementSchedulerConfig.settlementSchedulerConfigId.settlementModelId.eq(
                                                                settlementModelId))
                                                        .fetch();

        List<SchedulerConfigData> settlementSchedulerList = new ArrayList<>();

        if (tupleSQLQuery == null || tupleSQLQuery.isEmpty()) {
            return settlementSchedulerList;
        }

        for (Tuple tuple : tupleSQLQuery) {
            settlementSchedulerList.add(new SchedulerConfigData(tuple.get(schedulerConfig.schedulerConfigId),
                                                                tuple.get(schedulerConfig.name),
                                                                tuple.get(schedulerConfig.jobName),
                                                                tuple.get(schedulerConfig.cronExpression),
                                                                tuple.get(schedulerConfig.description),
                                                                tuple.get(schedulerConfig.zoneId),
                                                                tuple.get(schedulerConfig.active)));
        }

        return settlementSchedulerList;

    }

}
