//package com.thitsaworks.operation_portal.core.hubuser.command.impl;
//
//import com.querydsl.core.QueryResults;
//import com.querydsl.sql.SQLQuery;
//import com.querydsl.sql.SQLQueryFactory;
//import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
//import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
//import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
//import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncements;
//import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
//import com.thitsaworks.component.common.identifier.AnnouncementId;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.parsing.QualifierEntry;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class RemoveAnnouncementsBean implements RemoveAnnouncements {
//
//    private static final Logger LOG = LoggerFactory.getLogger(RemoveAnnouncementsBean.class);
//
//    private AnnouncementRepository announcementRepository;
//
//
//    @Autowired
//    @Qualifier(PersistenceQualifiers.Core.JPA_QUERY_FACTORY)
//    private SQLQueryFactory readQueryFactory;
//
//    @Override
//    @DfspWriteTransactional
//    public RemoveAnnouncements.Output execute(RemoveAnnouncements.Input input) {
//
//        LocalDate currentDate = LocalDate.now();
//        LocalDate currentDateMinus6Months = currentDate.minusMonths(6);
//        Instant removeDate = currentDateMinus6Months.atStartOfDay(ZoneId.systemDefault()).toInstant();
//
//         tblAnnouncement = DfspTblAnnouncement.tblAnnouncement;
//
//        SQLQuery<Long> sqlAnnouncementQuery =
//                this.readQueryFactory.select(tblAnnouncement.announcementId).from(tblAnnouncement)
//                                     .where(tblAnnouncement.announcementDate.lt(removeDate.getEpochSecond()));
//
//        QueryResults<Long> announcementResults = sqlAnnouncementQuery.fetchResults();
//
//        Set<AnnouncementId> announcementIds = new HashSet<>();
//
//        if (announcementResults != null && !announcementResults.isEmpty()) {
//
//            for (Long id : announcementResults.getResults()) {
//                announcementIds.add(new AnnouncementId(id));
//            }
//        }
//
//        //this.announcementRepository.deleteAllByIdInBatch(announcementIds);
//
//        List<Announcement> announcementList = this.announcementRepository.findAllById(announcementIds);
//
//        for (Announcement announcement : announcementList) {
//
//            this.announcementRepository.save(announcement.isDeleted(true));
//        }
//
//        return new RemoveAnnouncements.Output(true);
//    }
//
//}
