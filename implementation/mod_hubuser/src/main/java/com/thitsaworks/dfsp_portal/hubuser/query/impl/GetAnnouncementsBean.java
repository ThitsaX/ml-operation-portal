package com.thitsaworks.dfsp_portal.hubuser.query.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.datasource.persistence.ReadTransactional;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.dfsp_portal.hubuser.query.GetAnnouncements;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblAnnouncement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@ReadTransactional
public class GetAnnouncementsBean implements GetAnnouncements {

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    public GetAnnouncements.Output execute(GetAnnouncements.Input input) {

        DfspTblAnnouncement tblAnnouncement = DfspTblAnnouncement.tblAnnouncement;

        SQLQuery<Tuple> tupleSQLQuery =
                this.readQueryFactory.select(tblAnnouncement.announcementId, tblAnnouncement.announcementTitle,
                            tblAnnouncement.announcementDetail, tblAnnouncement.announcementDate, tblAnnouncement.isDeleted)
                                     .from(tblAnnouncement)
                                     .where(tblAnnouncement.isDeleted.eq(false));

        QueryResults<Tuple> results = tupleSQLQuery.fetchResults();

        if (results == null || results.isEmpty()) {

            return new GetAnnouncements.Output(new ArrayList<>());
        }

        List<Output.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        for (Tuple tuple : results.getResults()) {

            announcementInfoList.add(new GetAnnouncements.Output.AnnouncementInfo(
                    new AnnouncementId(tuple.get(tblAnnouncement.announcementId)),
                    tuple.get(tblAnnouncement.announcementTitle),
                    tuple.get(tblAnnouncement.announcementDetail),
                    Instant.ofEpochSecond(tuple.get(tblAnnouncement.announcementDate)),
                    tuple.get(tblAnnouncement.isDeleted)));
        }

        return new GetAnnouncements.Output(announcementInfoList);
    }

}
