package com.thitsaworks.operation_portal.participant.query.impl;

import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.GetParticipant;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ReadTransactional
public class GetParticipantBean implements GetParticipant {

    @Autowired
    private SQLQueryFactory sqlQueryFactory;

    @Override
    public GetParticipant.Output execute(GetParticipant.Input input) throws ParticipantNotFoundException {

        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;

        SQLQuery<Tuple> tupleSQLQuery =
                this.sqlQueryFactory.select(tblParticipant.participantId,
                            tblParticipant.dfspCode, tblParticipant.name, tblParticipant.address, tblParticipant.mobile,
                            tblParticipant.businessContactId, tblParticipant.technicalContactId, tblParticipant.createdDate)
                                    .from(tblParticipant)
                                    .where(tblParticipant.participantId.eq(input.getParticipantId().getId()));

        Tuple result = tupleSQLQuery.fetchOne();

        if (result == null) {

            throw new ParticipantNotFoundException(input.getParticipantId().getId().toString());
        }

        return new GetParticipant.Output(
                new ParticipantId(result.get(tblParticipant.participantId)),
                result.get(tblParticipant.dfspCode),
                result.get(tblParticipant.name),
                result.get(tblParticipant.address),
                new Mobile(result.get(tblParticipant.mobile)),
                new ContactId(result.get(tblParticipant.businessContactId)),
                new ContactId(result.get(tblParticipant.technicalContactId)),
                Instant.ofEpochSecond(result.get(tblParticipant.createdDate)));
    }

}
