package com.thitsaworks.operation_portal.report.query.impl;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.report.domain.SettlementDetailReport;
import com.thitsaworks.operation_portal.report.query.GenerateDetailRpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CentralLedgerReadTransactional
public class GenerateDetailRptBean implements GenerateDetailRpt {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateDetailRptBean.class);

    @Autowired
    private SettlementDetailReport settlementDetailReport;

    @Override
    public Output execute(Input input) throws Exception {

       return new Output(settlementDetailReport.generate(input.getSettlementId(),input.getFspId(),input.getFileType(),input.getTimezoneOffset()));

    }
}

