package com.thitsaworks.dfsp_portal.hubuser.domain.command.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.hubuser.domain.Announcement;
import com.thitsaworks.dfsp_portal.hubuser.domain.command.RemoveAnnouncements;
import com.thitsaworks.dfsp_portal.hubuser.domain.repository.AnnouncementRepository;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import com.thitsaworks.dfsp_portal.querydsl.dbtable.DfspTblAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RemoveAnnouncementsBean implements RemoveAnnouncements {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveAnnouncementsBean.class);

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    @Qualifier("readQueryFactory")
    private SQLQueryFactory readQueryFactory;

    @Override
    @WriteTransactional
    public RemoveAnnouncements.Output execute(RemoveAnnouncements.Input input) {

        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus6Months = currentDate.minusMonths(6);
        Instant removeDate = currentDateMinus6Months.atStartOfDay(ZoneId.systemDefault()).toInstant();

        DfspTblAnnouncement tblAnnouncement = DfspTblAnnouncement.tblAnnouncement;

        SQLQuery<Long> sqlAnnouncementQuery =
                this.readQueryFactory.select(tblAnnouncement.announcementId).from(tblAnnouncement)
                                     .where(tblAnnouncement.announcementDate.lt(removeDate.getEpochSecond()));

        QueryResults<Long> announcementResults = sqlAnnouncementQuery.fetchResults();

        Set<AnnouncementId> announcementIds = new HashSet<>();

        if (announcementResults != null && !announcementResults.isEmpty()) {

            for (Long id : announcementResults.getResults()) {
                announcementIds.add(new AnnouncementId(id));
            }
        }

        //this.announcementRepository.deleteAllByIdInBatch(announcementIds);

        List<Announcement> announcementList = this.announcementRepository.findAllById(announcementIds);

        for (Announcement announcement : announcementList) {

            this.announcementRepository.save(announcement.isDeleted(true));
        }

        return new RemoveAnnouncements.Output(true);
    }

}
