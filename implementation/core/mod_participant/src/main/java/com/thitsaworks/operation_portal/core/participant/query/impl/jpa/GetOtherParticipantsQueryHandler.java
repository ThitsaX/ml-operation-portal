package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.model.QContact;
import com.thitsaworks.operation_portal.core.participant.model.QParticipant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.query.GetOtherParticipantsQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class GetOtherParticipantsQueryHandler implements GetOtherParticipantsQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetOtherParticipantsQueryHandler.class);

    private final ParticipantRepository participantRepository;

    private final QParticipant participant = QParticipant.participant;

    private final QContact businessContact = QContact.contact;

    private final QContact technicalContact = QContact.contact;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ParticipantInfo> getOtherParticipants(ParticipantId participantId) {

        SimplePath<String> dfspCodePath =
                Expressions.path(String.class, participant.dfspCode, "value");

        JPAQuery<Tuple> tupleSQLQuery =
                this.jpaQueryFactory.select(participant.participantId,
                                            participant.dfspCode,
                                            participant.name,
                                            participant.address,
                                            participant.mobile,
                                            businessContact.name.as("BusinessName"),
                                            businessContact.mobile.as("BusinessMobile"),
                                            technicalContact.name,
                                            technicalContact.mobile,
                                            participant.createdAt)
                                    .from(participant)
                                    .leftJoin(businessContact)
                                    .on(participant.businessContact.contactId.eq(businessContact.contactId))
                                    .leftJoin(technicalContact)
                                    .on(participant.technicalContact.contactId.eq(technicalContact.contactId))
                                    .where(participant.participantId.notIn(participantId));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new ArrayList<>();
        }

        List<ParticipantInfo> participantInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            participantInfoList.add(
                    new ParticipantInfo(
                            tuple.get(participant.participantId),
                            tuple.get(participant.dfspCode),
                            tuple.get(participant.name)));
        }

        return participantInfoList;

    }

}
