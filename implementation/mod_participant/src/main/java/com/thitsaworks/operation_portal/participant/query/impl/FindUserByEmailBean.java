package com.thitsaworks.operation_portal.participant.query.impl;

import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.operation_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.participant.query.FindUserByEmail;
import com.thitsaworks.operation_portal.querydsl.dbtable.DfspTblParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ReadTransactional
public class FindUserByEmailBean implements FindUserByEmail {

    private static final Logger LOG = LoggerFactory.getLogger(FindUserByEmailBean.class);

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public Output execute(Input input) {

        DfspTblParticipantUser tblUser = DfspTblParticipantUser.tblParticipantUser;

        SQLQuery<Long> tupleSQLQuery = this.readQueryFactory.select(tblUser.userId)
                                                            .from(tblUser)
                                                            .where(tblUser.email.eq(input.getEmail().getValue())
                                                                                .and(tblUser.isDeleted.isFalse()));

        Long tuple = tupleSQLQuery.fetchOne();

        return new FindUserByEmail.Output(
                tuple == null ? Optional.ofNullable(null) : Optional.of(new ParticipantUserId(tuple)));
    }

}
