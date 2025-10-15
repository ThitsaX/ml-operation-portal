package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.HubSettlementModelData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.HubSettlementModelQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HubSettlementModelJdbcQueryHandler implements HubSettlementModelQuery {

    private static final Logger LOG = LoggerFactory.getLogger(HubSettlementModelJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HubSettlementModelJdbcQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<HubSettlementModelData> getSettlementModelList() {

        return this.jdbcTemplate.query(
                "SELECT sm.settlementModelId, sm.name, sm.isActive, sg.name AS settlementGranularityName, si.name AS settlementInterchangeName, sm.settlementDelayId, sm.currencyId, sm.requireLiquidityCheck, sm.ledgerAccountTypeId, sm.autoPositionReset, sm.adjustPosition, sm.settlementAccountTypeId \n" +
                    "FROM settlementModel AS sm JOIN settlementInterchange AS si ON sm.settlementInterchangeId = si.settlementInterchangeId \n" +
                    "JOIN settlementGranularity AS sg ON sm.settlementGranularityId = sg.settlementGranularityId",
                (rs, rowNum) -> new HubSettlementModelData(
                        rs.getInt("settlementModelId"),
                        rs.getString("name"),
                        rs.getBoolean("isActive"),
                        rs.getString("settlementGranularityName"),
                        rs.getString("settlementInterchangeName"),
                        rs.getInt("settlementDelayId"),
                        rs.getString("currencyId"),
                        rs.getBoolean("requireLiquidityCheck"),
                        rs.getInt("ledgerAccountTypeId"),
                        rs.getBoolean("autoPositionReset"),
                        rs.getBoolean("adjustPosition"),
                        rs.getInt("settlementAccountTypeId")
                ));

    }

    @Override
    public HubSettlementModelData getByName(String name) throws HubServicesException {

        List<HubSettlementModelData> hubSettlementModelDataList = this.jdbcTemplate.query(
            "SELECT sm.settlementModelId, sm.name, sm.isActive, sg.name as settlementGranularityName, si.name as settlementInterchangeName, sm.settlementDelayId, sm.currencyId, sm.requireLiquidityCheck, sm.ledgerAccountTypeId, sm.autoPositionReset, sm.adjustPosition, sm.settlementAccountTypeId \n" +
                "FROM settlementModel as sm JOIN settlementInterchange as si ON sm.settlementInterchangeId = si.settlementInterchangeId \n" +
                "JOIN settlementGranularity as sg ON sm.settlementGranularityId = sg.settlementGranularityId WHERE name = ?",
                new Object[]{name},
                (rs, rowNum) -> new HubSettlementModelData(
                        rs.getInt("settlementModelId"),
                        rs.getString("name"),
                        rs.getBoolean("isActive"),
                        rs.getString("settlementGranularityName"),
                        rs.getString("settlementInterchangeName"),
                        rs.getInt("settlementDelayId"),
                        rs.getString("currencyId"),
                        rs.getBoolean("requireLiquidityCheck"),
                        rs.getInt("ledgerAccountTypeId"),
                        rs.getBoolean("autoPositionReset"),
                        rs.getBoolean("adjustPosition"),
                        rs.getInt("settlementAccountTypeId")
                ));

        if (hubSettlementModelDataList.isEmpty()) {
            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_ERROR.description(
                    "Settlement model with name [" + name + "] cannot find on Hub"));
        }

        return hubSettlementModelDataList.getFirst();

    }

}
