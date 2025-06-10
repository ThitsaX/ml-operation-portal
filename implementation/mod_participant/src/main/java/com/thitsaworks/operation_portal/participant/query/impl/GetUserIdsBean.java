package com.thitsaworks.operation_portal.participant.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.participant.query.GetUserIds;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipant;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetUserIdsBean implements GetUserIds {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspTblParticipantUser tblUser = DfspTblParticipantUser.tblParticipantUser;
        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;

        SQLQuery<Long> tupleSQLQuery = this.readQueryFactory.select(tblUser.userId).from(tblUser).join(tblParticipant)
                                                            .on(tblUser.participantId.eq(tblParticipant.participantId))
                                                            .where(tblUser.participantId.eq(input.getParticipantId()
                                                                                                 .getId()));

        QueryResults<Long> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<ParticipantUserId> userInfoList = new ArrayList<>();

        for (Long tuple : results.getResults()) {

            userInfoList.add(new ParticipantUserId(tuple));
        }

        return new Output(userInfoList);
    }

}
