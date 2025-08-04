package com.thitsaworks.operation_portal.core.audit.query.impl.jpa;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditByParticipantAndUserQuery;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.participant.model.QParticipant;
import com.thitsaworks.operation_portal.core.participant.model.QParticipantUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetAuditByParticipantAndUserJpaQueryHandler implements GetAuditByParticipantAndUserQuery {

    private final JPAQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        QParticipantUser participantUser = QParticipantUser.participantUser;
        QParticipant participant = QParticipant.participant;
        QAction action = QAction.action;
        QAudit audit = QAudit.audit;

        var actionCode = input.getActionName() == null ? null : new ActionCode(input.getActionName());

        JPAQuery<Tuple> tupleSQLQuery =
            this.readQueryFactory.select(participant.description,
                                         participantUser.name,
                                         action.actionCode,
                                         audit.inputInfo,
                                         audit.outputInfo,
                                         audit.createdAt)
                                 .from(audit)
                                 .join(participant)
                                 .on(participant.participantId.id.eq(audit.realmId.id))
                                 .leftJoin(participantUser)
                                 .on(participantUser.participantUserId.id.eq(audit.userId.id))
                                 .join(action)
                                 .on(action.actionId.eq(audit.actionId))
                                 .where(input.getRealmId() == null ?
                                            audit.realmId.eq(audit.realmId) :
                                            audit.realmId.id.eq(input.getRealmId()
                                                                     .getId())
                                                            .and(input.getUserId() ==
                                                                     null ?
                                                                     audit.userId.eq(
                                                                         audit.userId) :
                                                                     audit.userId.id.eq(
                                                                         input.getUserId()
                                                                              .getId()))
                                                            .and(input.getFromDate() == null ||
                                                                     input.getToDate() == null ?
                                                                     audit.createdAt.eq(audit.createdAt) :
                                                                     audit.createdAt.between(input.getFromDate(),
                                                                                             input.getToDate()))
                                                            .and(actionCode == null
                                                                     ? action.actionCode.isNotNull()
                                                                     : action.actionCode.eq(actionCode)));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<Output.AuditInfo> auditInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            auditInfoList.add(new Output.AuditInfo(tuple.get(participant.description),
                                                   tuple.get(participantUser.name),
                                                   Objects.requireNonNull(tuple.get(action.actionCode))
                                                          .getValue(),
                                                   tuple.get(audit.inputInfo),
                                                   tuple.get(audit.outputInfo),
                                                   Instant.ofEpochSecond(tuple.get(audit.createdAt)
                                                                              .getEpochSecond())));
        }

        return new Output(auditInfoList);

    }

}
