package com.thitsaworks.operation_portal.participant.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.GetContacts;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblContact;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetContactsBean implements GetContacts {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public GetContacts.Output execute(GetContacts.Input input) throws ParticipantNotFoundException {

        DfspTblParticipant tblParticipant = DfspTblParticipant.tblParticipant;
        DfspTblContact tblContact = DfspTblContact.tblContact;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblContact.contactId, tblContact.participantId, tblContact.name,
                            tblContact.title, tblContact.email, tblContact.mobile, tblParticipant.businessContactId,
                            tblParticipant.technicalContactId)
                                    .from(tblContact)
                                    .join(tblParticipant)
                                    .on(tblParticipant.participantId.eq(tblContact.participantId))
                                    .where(tblContact.participantId.eq(input.getParticipantId().getId()));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new GetContacts.Output(new ArrayList<>());
        }

        List<GetContacts.Output.ContactInfo> contactInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            contactInfoList.add(
                    new GetContacts.Output.ContactInfo(new ParticipantId(tuple.get(tblContact.participantId)),
                            new ContactId(tuple.get(tblContact.contactId)), tuple.get(tblContact.name),
                            tuple.get(tblContact.title), new Email(tuple.get(tblContact.email)),
                            new Mobile(tuple.get(tblContact.mobile)),
                            (tuple.get(tblParticipant.businessContactId) != null &&
                                    tuple.get(tblParticipant.businessContactId)
                                         .equals(tuple.get(tblContact.contactId))) ? "Business" :
                                    (tuple.get(tblParticipant.technicalContactId) != null &&
                                            tuple.get(tblParticipant.technicalContactId)
                                                 .equals(tuple.get(tblContact.contactId))) ? "Technical" : ""));
        }

        return new GetContacts.Output(contactInfoList);
    }

}
