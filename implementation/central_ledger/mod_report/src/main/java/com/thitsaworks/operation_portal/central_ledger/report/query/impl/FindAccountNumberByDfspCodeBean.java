package com.thitsaworks.operation_portal.central_ledger.report.query.impl;

import com.thitsaworks.operation_portal.central_ledger.report.query.FindAccountNumberByDfspCode;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CentralLedgerTransactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CentralLedgerTransactional
public class FindAccountNumberByDfspCodeBean implements FindAccountNumberByDfspCode {

    private static final Logger LOG = LoggerFactory.getLogger(FindAccountNumberByDfspCodeBean.class);

    @Override
    public Output execute(Input input) {
        return null;
    }

//        DfspTblLiquidityProfile tblLiquidityProfile = DfspTblLiquidityProfile.tblLiquidityProfile;
//        DfspTblParticipant tblParticipant= DfspTblParticipant.tblParticipant;
//
//        String dfspCode= input.getDfspCode();
//        String currencyId= input.getCurrencyId();
//
//        SQLQuery<String> tupleSQLQuery =
//                this.readQueryFactory.select(tblLiquidityProfile.accountNumber)
//                        .from(tblLiquidityProfile)
//                        .join(tblParticipant)
//                        .on(tblParticipant.participantId.eq(tblLiquidityProfile.participantId))
//                        .where(tblLiquidityProfile.currency.eq(currencyId).and(tblParticipant.dfspCode.eq(dfspCode)));
//
//
//        String tuple = tupleSQLQuery.fetchOne();
//
//        return new FindAccountNumberByDfspCode.Output(
//                tuple == null ?  tuple="000000000000": ((tuple)));
//    }

}
