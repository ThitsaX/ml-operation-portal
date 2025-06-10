package com.thitsaworks.operation_portal.participant.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.GetOtherParticipants;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblContact;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetOtherParticipantsBean implements GetOtherParticipants {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;
        DfspTblContact tblBusinessContact = DfspTblContact.tblContact;
        DfspTblContact tblTechnicalContact = DfspTblContact.tblContact;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblParticipant.participantId,
                            tblParticipant.dfspCode, tblParticipant.name, tblParticipant.address, tblParticipant.mobile,
                            tblBusinessContact.name.as("BusinessName"), tblBusinessContact.mobile.as("BusinessMobile"), tblTechnicalContact.name,
                            tblTechnicalContact.mobile, tblParticipant.createdDate)
                                    .from(tblParticipant)
                                    .leftJoin(tblBusinessContact)
                                    .on(tblParticipant.businessContactId.eq(tblBusinessContact.contactId))
                                    .leftJoin(tblTechnicalContact)
                                    .on(tblParticipant.technicalContactId.eq(tblTechnicalContact.contactId))
                        .where(tblParticipant.participantId.notIn(input.getParticipantId().getId()))
                        .orderBy(tblParticipant.dfspCode.asc());

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            participantInfoList.add(
                    new Output.ParticipantInfo(
                            new ParticipantId(tuple.get(tblParticipant.participantId)),
                            tuple.get(tblParticipant.dfspCode),
                            tuple.get(tblParticipant.name)));
        }

        return new Output(participantInfoList);
    }

}
