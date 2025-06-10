package com.thitsaworks.operation_portal.participant.query.impl;

import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.participant.query.GetUser;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipant;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ReadTransactional
public class GetUserBean implements GetUser {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) throws ParticipantUserNotFoundException {

        DfspTblParticipantUser tblParticipantUser = DfspTblParticipantUser.tblParticipantUser;
        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblParticipantUser.userId, tblParticipantUser.name,
                            tblParticipantUser.email, tblParticipantUser.firstName, tblParticipantUser.lastName,
                            tblParticipantUser.jobTitle, tblParticipantUser.participantId,
                            tblParticipantUser.createdDate, tblParticipant.dfspCode,tblParticipant.dfspName).from(tblParticipantUser)
                                     .join(tblParticipant)
                                     .on(tblParticipant.participantId.eq(tblParticipantUser.participantId))
                                     .where(tblParticipantUser.userId.eq(input.getParticipantUserId().getId()));

        Tuple result = tupleSQLQuery.fetchOne();

        if (result == null) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());
        }

        return new Output(new ParticipantUserId(result.get(tblParticipantUser.userId)),
                result.get(tblParticipantUser.name),
                          new Email(result.get(tblParticipantUser.email)),
                          result.get(tblParticipantUser.firstName),
                          result.get(tblParticipantUser.lastName),
                          result.get(tblParticipantUser.jobTitle),
                new ParticipantId(result.get((tblParticipantUser.participantId))),
                          Instant.ofEpochSecond(result.get(tblParticipantUser.createdDate)),
                          result.get(tblParticipant.dfspCode),
                          result.get(tblParticipant.dfspName));

    }

}
