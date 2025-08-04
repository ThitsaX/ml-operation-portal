package com.thitsaworks.operation_portal.core.audit.query.impl.jpa;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.participant.model.QParticipant;
import com.thitsaworks.operation_portal.core.participant.model.QParticipantUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.query.GetAllAuditByParticipantQuery;
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

        QParticipantUser participantUser = QParticipantUser.participantUser;
        QParticipant participant = QParticipant.participant;
        QAction action = QAction.action;
        QAudit audit = QAudit.audit;

        var actionCode = input.actionName() == null ? null : new ActionCode(input.actionName());


        JPAQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(participant.description, participantUser.name, action.actionCode, audit.createdAt).from(
                            audit).join(participant)
                                     .on(participant.participantId.id.eq(audit.realmId.id)).leftJoin(participantUser)
                                     .on(participantUser.participantUserId.id.eq(audit.userId.id)).join(action)
                                     .on(action.actionId.eq(audit.actionId)).where(input.realmId() == null ?
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
                                        .and(input.userId() == null ? audit.userId.eq(audit.userId)
                                                                    : audit.userId.id.eq(input.userId().getId()))
                                                                                                           .and(input.actionName() == null ? action.actionCode.eq(action.actionCode)
                                                                    : action.actionCode.eq(actionCode)))
                                     .orderBy(audit.createdAt.desc());

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new  Output(new ArrayList<>());
        }

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            auditInfoList.add(new Output.AuditInfo(
                tuple.get(participantUser.name),
                Objects.requireNonNull(tuple.get(action.actionCode))
                       .getValue(),
                tuple.get(audit.createdAt)));
        }

        return new Output(auditInfoList);

    }

}
