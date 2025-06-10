package com.thitsaworks.operation_portal.report.query.impl;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.report.domain.SettlementStatement;
import com.thitsaworks.operation_portal.report.query.FindAccountNumberByDfspCode;
import com.thitsaworks.operation_portal.report.query.GenerateStatementRpt;
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

