package com.thitsaworks.dfsp_portal.participant.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.query.GetParticipants;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblContact;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetParticipantsBean implements GetParticipants {

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
                            tblParticipant.dfspCode, tblParticipant.dfspName,tblParticipant.name, tblParticipant.address, tblParticipant.mobile,
                            tblBusinessContact.name.as("BusinessName"), tblBusinessContact.mobile.as("BusinessMobile"), tblTechnicalContact.name,
                            tblTechnicalContact.mobile, tblParticipant.createdDate)
                                    .from(tblParticipant)
                                    .leftJoin(tblBusinessContact)
                                    .on(tblParticipant.businessContactId.eq(tblBusinessContact.contactId))
                                    .leftJoin(tblTechnicalContact)
                                    .on(tblParticipant.technicalContactId.eq(tblTechnicalContact.contactId))
                ;

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new GetParticipants.Output(new ArrayList<>());
        }

        List<Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            participantInfoList.add(
                    new GetParticipants.Output.ParticipantInfo(
                            new ParticipantId(tuple.get(tblParticipant.participantId)),
                            tuple.get(tblParticipant.dfspCode),
                            tuple.get(tblParticipant.dfspName),
                            tuple.get(tblParticipant.name),
                            tuple.get(tblParticipant.address),
                            new Mobile(tuple.get(tblParticipant.mobile)),
                            tuple.get(tblBusinessContact.name) + " (" + tuple.get(tblBusinessContact.mobile) + ")",
                            tuple.get(tblTechnicalContact.name) + " (" + tuple.get(tblTechnicalContact.mobile) + ")",
                            Instant.ofEpochSecond(tuple.get(tblParticipant.createdDate))));
        }

        return new Output(participantInfoList);
    }

}
