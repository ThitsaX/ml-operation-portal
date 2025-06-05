package com.thitsa.dfsp_portal.report.query.impl;

import com.thitsa.dfsp_portal.report.domain.SettlementStatement;
import com.thitsa.dfsp_portal.report.query.FindAccountNumberByDfspCode;
import com.thitsa.dfsp_portal.report.query.GenerateStatementRpt;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CentralLedgerReadTransactional
public class GenerateStatementRptBean implements GenerateStatementRpt {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementRptBean.class);

    @Autowired
    private FindAccountNumberByDfspCode findAccountNumberByDfspCode;

    @Autowired
    private SettlementStatement settlementStatement;

    @Override
    public Output execute(Input input) throws Exception {

        FindAccountNumberByDfspCode.Output findAccountNumber =
                this.findAccountNumberByDfspCode.execute(new FindAccountNumberByDfspCode.Input(input.getFspId(),input.getCurrencyId()));

        String accountNumber =findAccountNumber.getAccountNumber();

         return new Output(settlementStatement.generate(input.getFspId(),input.getStartDate().toString(),input.getEndDate().toString(), accountNumber,input.getFiletype(),input.getTimeZoneOffset(),input.getCurrencyId()));

    }
}

