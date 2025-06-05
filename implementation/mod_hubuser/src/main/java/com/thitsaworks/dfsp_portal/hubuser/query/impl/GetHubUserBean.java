package com.thitsaworks.dfsp_portal.hubuser.query.impl;

import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.hubuser.exception.HubUserNotFoundException;
import com.thitsaworks.dfsp_portal.hubuser.identity.HubUserId;
import com.thitsaworks.dfsp_portal.hubuser.query.GetHubUser;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ReadTransactional
public class GetHubUserBean implements GetHubUser {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) throws HubUserNotFoundException {

        DfspTblHubUser tblHubUser = DfspTblHubUser.tblHubUser;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblHubUser.userId, tblHubUser.name, tblHubUser.email, tblHubUser.firstName,
                            tblHubUser.lastName, tblHubUser.jobTitle,
                            tblHubUser.createdDate).from(tblHubUser)
                                     .where(tblHubUser.userId.eq(input.getHubUserId().getId()));

        Tuple result = tupleSQLQuery.fetchOne();

        if (result == null) {

            throw new HubUserNotFoundException(input.getHubUserId().getId().toString());
        }

        return new Output(new HubUserId(result.get(tblHubUser.userId)),
                result.get(tblHubUser.name),
                new Email(result.get(tblHubUser.email)), result.get(tblHubUser.firstName),
                result.get(tblHubUser.lastName), result.get(tblHubUser.jobTitle),
                Instant.ofEpochSecond(result.get(tblHubUser.createdDate)));

    }

}
