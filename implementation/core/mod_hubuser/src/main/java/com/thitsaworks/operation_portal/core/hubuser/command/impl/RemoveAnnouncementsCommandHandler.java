package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncementsCommand;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.QAnnouncement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RemoveAnnouncementsCommandHandler implements RemoveAnnouncementsCommand {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveAnnouncementsCommandHandler.class);

    private final AnnouncementRepository announcementRepository;

    private final QAnnouncement announcement = QAnnouncement.announcement;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @CoreWriteTransactional
    public RemoveAnnouncementsCommand.Output execute(RemoveAnnouncementsCommand.Input input) {

        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus6Months = currentDate.minusMonths(6);
        Instant removeDate = currentDateMinus6Months.atStartOfDay(ZoneId.systemDefault()).toInstant();

        JPAQuery<Long> sqlAnnouncementQuery =
                this.jpaQueryFactory.select(announcement.announcementId.id).from(announcement)
                                    .where(announcement.announcementDate.lt(removeDate));

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

        return new RemoveAnnouncementsCommand.Output(true);
    }

}
