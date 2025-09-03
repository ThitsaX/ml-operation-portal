package com.thitsaworks.operation_portal.core.audit.query.impl.jpa;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.participant.model.QParticipant;
import com.thitsaworks.operation_portal.core.participant.model.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetAllAuditByParticipantJpaQueryHandler implements GetAllAuditByParticipantQuery {

    private final JPAQueryFactory readQueryFactory;

    @Override
    @CoreReadTransactional
    public Output execute(Input input) {

        QUser user = QUser.user;
        QParticipant participant = QParticipant.participant;
        QAction action = QAction.action;
        QAudit audit = QAudit.audit;

        JPAQuery<Tuple> tupleSQLQuery =
            this.readQueryFactory.select(participant.description, user.email, action.actionCode, audit.createdAt)
                                 .from(
                                     audit)
                                 .join(participant)
                                 .on(participant.participantId.id.eq(audit.realmId.id))
                                 .leftJoin(user)
                                 .on(user.userId.id.eq(audit.userId.id))
                                 .join(action)
                                 .on(action.actionId.eq(audit.actionId))
                                 .where(input.realmId() == null ?
                                            audit.realmId.eq(audit.realmId) :
                                            audit.realmId.id.eq(input.realmId()
                                                                     .getId())
                                                            .and(input.fromDate() ==
                                                                     null ||
                                                                     input.toDate() ==
                                                                         null ?
                                                                     audit.createdAt.eq(audit.createdAt) :
                                                                     audit.createdAt.between(
                                                                         input.fromDate(),
                                                                         input.toDate()))
                                                            .and(action.actionId.in(input.grantedActionList())))
                                 .orderBy(audit.createdAt.desc());

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            auditInfoList.add(new Output.AuditInfo(
                tuple.get(audit.createdAt),
                Objects.requireNonNull(tuple.get(action.actionCode))
                       .getValue(),
                tuple.get(user.email)));
        }

        return new Output(auditInfoList);

    }

}
