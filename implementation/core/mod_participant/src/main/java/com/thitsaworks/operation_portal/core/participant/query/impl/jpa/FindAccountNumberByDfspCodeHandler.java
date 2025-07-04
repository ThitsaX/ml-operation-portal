package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.model.QLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.model.QParticipant;
import com.thitsaworks.operation_portal.core.participant.query.FindAccountNumberByDfspCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@CoreReadTransactional
@RequiredArgsConstructor
public class FindAccountNumberByDfspCodeHandler implements FindAccountNumberByDfspCode {

    private static final Logger LOG = LoggerFactory.getLogger(FindAccountNumberByDfspCodeHandler.class);

    private final QLiquidityProfile liquidityProfile = QLiquidityProfile.liquidityProfile;

    private final QParticipant participant = QParticipant.participant;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspCode dfspCode = new DfspCode(input.dfspCode());
        String currencyId = input.currencyId();

        JPAQuery<String> tupleSQLQuery =
                this.jpaQueryFactory.select(liquidityProfile.accountNumber)
                                    .from(liquidityProfile)
                                    .join(participant)
                                    .on(participant.participantId.eq(liquidityProfile.participant.participantId))
                                    .where(liquidityProfile.currency.eq(currencyId)
                                                                    .and(participant.dfspCode.eq(dfspCode)));

        String tuple = tupleSQLQuery.fetchOne();

        return new FindAccountNumberByDfspCode.Output(
                tuple == null ? tuple = "000000000000" : ((tuple)));
    }

}
