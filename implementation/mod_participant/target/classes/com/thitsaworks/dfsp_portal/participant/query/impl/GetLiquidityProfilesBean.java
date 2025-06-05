package com.thitsaworks.dfsp_portal.participant.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.query.GetLiquidityProfiles;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblLiquidityProfile;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetLiquidityProfilesBean implements GetLiquidityProfiles {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) throws ParticipantNotFoundException {

        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;
        DfspTblLiquidityProfile tblLiquidityProfile = DfspTblLiquidityProfile.tblLiquidityProfile;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblLiquidityProfile.liquidityProfileId, tblLiquidityProfile.participantId,
                            tblLiquidityProfile.accountName, tblLiquidityProfile.accountNumber,
                            tblLiquidityProfile.currency, tblLiquidityProfile.isActive)
                                     .from(tblLiquidityProfile)
                                     .join(tblParticipant)
                                     .on(tblParticipant.participantId.eq(tblLiquidityProfile.participantId))
                                     .where(tblLiquidityProfile.participantId.eq(input.getParticipantId().getId()));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<Output.LiquidityProfileInfo> liquidityProfileInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            liquidityProfileInfoList.add(new Output.LiquidityProfileInfo(
                    new ParticipantId(tuple.get(tblLiquidityProfile.participantId)),
                    new LiquidityProfileId(tuple.get(tblLiquidityProfile.liquidityProfileId)),
                    tuple.get(tblLiquidityProfile.accountName), tuple.get(tblLiquidityProfile.accountNumber),
                    tuple.get(tblLiquidityProfile.currency),
                    tuple.get(tblLiquidityProfile.isActive)));
        }

        return new Output(liquidityProfileInfoList);
    }

}
