package com.thitsaworks.operation_portal.dfsp_portal.hubuser.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.identity.HubUserId;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.query.GetHubUsers;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetHubUsersBean implements GetHubUsers {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspTblHubUser tblHubUser = DfspTblHubUser.tblHubUser;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblHubUser.userId, tblHubUser.name, tblHubUser.email, tblHubUser.firstName,
                            tblHubUser.lastName, tblHubUser.jobTitle, tblHubUser.createdDate).from(tblHubUser)
                                     .where(tblHubUser.isDeleted.eq(false));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        List<Output.HubUserInfo> userInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            userInfoList.add(new Output.HubUserInfo(new HubUserId(tuple.get(tblHubUser.userId)),
                    tuple.get(tblHubUser.name), new Email(tuple.get(tblHubUser.email)), tuple.get(tblHubUser.firstName),
                    tuple.get(tblHubUser.lastName), tuple.get(tblHubUser.jobTitle),
                    Instant.ofEpochSecond(tuple.get(tblHubUser.createdDate))));
        }

        return new Output(userInfoList);
    }

}
