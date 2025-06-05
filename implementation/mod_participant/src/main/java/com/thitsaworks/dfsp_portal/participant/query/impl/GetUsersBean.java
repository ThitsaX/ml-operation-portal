package com.thitsaworks.dfsp_portal.participant.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.participant.query.GetUsers;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblParticipantUser;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetUsersBean implements GetUsers {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspTblParticipantUser tblUser = DfspTblParticipantUser.tblParticipantUser;
        DfspTblPrincipal tblPrincipal = DfspTblPrincipal.tblPrincipal;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblUser.userId, tblUser.name, tblUser.email, tblUser.firstName,
                            tblUser.lastName, tblUser.jobTitle, tblUser.participantId, tblUser.createdDate,
                            tblPrincipal.status).from(tblUser).leftJoin(tblPrincipal)
                                     .on(tblUser.userId.eq(tblPrincipal.principalId))
                                     .where(input.getParticipantId() == null ?
                                             tblUser.participantId.eq(tblUser.participantId) :
                                             tblUser.participantId.eq(input.getParticipantId().getId())
                                                                  .and(tblUser.isDeleted.eq(false)));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<Output.UserInfo> userInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            userInfoList.add(new Output.UserInfo(new ParticipantUserId(tuple.get(tblUser.userId)),
                                                 tuple.get(tblUser.name),
                                                 new Email(tuple.get(tblUser.email)),
                                                 tuple.get(tblUser.firstName),
                                                 tuple.get(tblUser.lastName),
                                                 tuple.get(tblUser.jobTitle),
                                                 tuple.get(tblPrincipal.userRoleType),
                                                 tuple.get(tblPrincipal.status),
                            Instant.ofEpochSecond(tuple.get(tblUser.createdDate))));
        }

        return new Output(userInfoList);
    }

}
