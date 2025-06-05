package com.thitsaworks.dfsp_portal.hubuser.query.impl;

import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.hubuser.exception.AnnouncementNotFoundException;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.dfsp_portal.hubuser.query.GetAnnouncement;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblAnnouncement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ReadTransactional
public class GetAnnouncementBean implements GetAnnouncement {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public GetAnnouncement.Output execute(GetAnnouncement.Input input) throws AnnouncementNotFoundException {

        DfspTblAnnouncement tblAnnouncement = DfspTblAnnouncement.tblAnnouncement;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblAnnouncement.announcementId, tblAnnouncement.announcementTitle,
                            tblAnnouncement.announcementDetail, tblAnnouncement.announcementDate, tblAnnouncement.isDeleted,
                            tblAnnouncement.createdDate).from(tblAnnouncement)
                                     .where(tblAnnouncement.announcementId.eq(input.getAnnouncementId().getId()));

        Tuple result = tupleSQLQuery.fetchOne();

        if (result == null) {

            throw new AnnouncementNotFoundException(input.getAnnouncementId().getId().toString());
        }

        return new GetAnnouncement.Output(new AnnouncementId(result.get(tblAnnouncement.announcementId)),
                result.get(tblAnnouncement.announcementTitle), result.get(tblAnnouncement.announcementDetail),
                Instant.ofEpochSecond(result.get(tblAnnouncement.announcementDate)),
                result.get(tblAnnouncement.isDeleted), Instant.ofEpochSecond(result.get(tblAnnouncement.createdDate)));

    }

}
