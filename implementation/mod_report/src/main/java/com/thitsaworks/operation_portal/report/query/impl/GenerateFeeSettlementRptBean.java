package com.thitsaworks.operation_portal.report.query.impl;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.report.domain.FeeSettlementReport;
import com.thitsaworks.operation_portal.report.query.GenerateFeeSettlementRpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CentralLedgerReadTransactional
public class GenerateFeeSettlementRptBean implements GenerateFeeSettlementRpt {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeSettlementRptBean.class);

    @Autowired
    private FeeSettlementReport feeSettlementReport;

    @Override
    public Output execute(Input input) throws Exception {

         return  new Output(feeSettlementReport.generate(input.getStartDate().toString(),input.getEndDate().toString(),input.getFromFsp(), input.getToFsp(),input.getCurrency(), input.getTimezone(),input.getFileType()))   ;

    }
}

