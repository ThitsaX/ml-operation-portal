package com.thitsa.dfsp_portal.report.query.impl;

import com.thitsa.dfsp_portal.report.domain.SettlementReport;
import com.thitsa.dfsp_portal.report.query.GenerateSettlementRpt;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CentralLedgerReadTransactional
public class GenerateSettlementRptBean implements GenerateSettlementRpt {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementRptBean.class);

    @Autowired
    private SettlementReport settlementReport;

    @Override
    public GenerateSettlementRpt.Output execute(GenerateSettlementRpt.Input input) throws Exception {

         return  new Output(settlementReport.generate(input.getFspId(), input.getSettlementId(), input.getFiletype(),input.getTimezoneOffset()));

    }
}
