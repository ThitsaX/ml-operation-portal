package com.thitsaworks.operation_portal.audit.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.query.GetAudit;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblAction;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblAudit;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipant;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetAuditBean implements GetAudit {

    @Autowired
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) throws UserNotFoundException {

        DfspTblParticipantUser tblUser = DfspTblParticipantUser.tblParticipantUser;
        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;
        DfspTblAction tblAction = DfspTblAction.tblAction;
        DfspTblAudit tblAudit = DfspTblAudit.tblAudit;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblParticipant.name, tblUser.name, tblAction.name, tblAudit.inputInfo,
                            tblAudit.outputInfo, tblAudit.createdDate).from(tblAudit).join(tblParticipant)
                                     .on(tblParticipant.participantId.eq(tblAudit.participantId)).leftJoin(tblUser)
                                     .on(tblUser.userId.eq(tblAudit.userId)).join(tblAction)
                                     .on(tblAction.actionId.eq(tblAudit.actionId)).where(input.getRealmId() == null ?
                                    tblAudit.participantId.eq(tblAudit.participantId) :
                                    tblAudit.participantId.eq(input.getRealmId().getId())
                                                          .and(input.getUserId() == null ? tblAudit.userId.eq(tblAudit.userId) :
                                                                  tblAudit.userId.eq(input.getUserId().getId()))
                                                          .and(input.getFromDate() == null || input.getToDate() == null ?
                                                                  tblAudit.createdDate.eq(tblAudit.createdDate) :
                                                                  tblAudit.createdDate.between(input.getFromDate().getEpochSecond(),
                                                                                   input.getToDate().getEpochSecond()))

                                     );
        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new  Output(new ArrayList<>());
        }

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            auditInfoList.add(new Output.AuditInfo( tuple.get(tblParticipant.name),
                    tuple.get(tblUser.name),tuple.get(tblAction.name),
                    tuple.get(tblAudit.inputInfo),tuple.get(tblAudit.outputInfo),
                    Instant.ofEpochSecond(tuple.get(tblAudit.createdDate))));
        }

        return new Output(auditInfoList);

    }

}
