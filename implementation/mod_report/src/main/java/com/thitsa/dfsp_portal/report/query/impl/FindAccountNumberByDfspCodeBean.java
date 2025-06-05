package com.thitsa.dfsp_portal.report.query.impl;

import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsa.dfsp_portal.report.query.FindAccountNumberByDfspCode;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblLiquidityProfile;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@ReadTransactional

public class FindAccountNumberByDfspCodeBean implements FindAccountNumberByDfspCode {

    private static final Logger LOG = LoggerFactory.getLogger(FindAccountNumberByDfspCodeBean.class);

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspTblLiquidityProfile tblLiquidityProfile = DfspTblLiquidityProfile.tblLiquidityProfile;
        DfspTblParticipant tblParticipant= DfspTblParticipant.tblParticipant;

        String dfspCode= input.getDfspCode();
        String currencyId= input.getCurrencyId();

        SQLQuery<String> tupleSQLQuery =
                this.readQueryFactory.select(tblLiquidityProfile.accountNumber)
                        .from(tblLiquidityProfile)
                        .join(tblParticipant)
                        .on(tblParticipant.participantId.eq(tblLiquidityProfile.participantId))
                        .where(tblLiquidityProfile.currency.eq(currencyId).and(tblParticipant.dfspCode.eq(dfspCode)));


        String tuple = tupleSQLQuery.fetchOne();

        return new FindAccountNumberByDfspCode.Output(
                tuple == null ?  tuple="000000000000": ((tuple)));
    }

}
