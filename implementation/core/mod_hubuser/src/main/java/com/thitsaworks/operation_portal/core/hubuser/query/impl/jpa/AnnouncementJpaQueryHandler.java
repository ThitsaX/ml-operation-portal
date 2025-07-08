package com.thitsaworks.operation_portal.core.hubuser.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.hubuser.data.AnnouncementData;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.QAnnouncement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import com.thitsaworks.operation_portal.core.hubuser.query.AnnouncementQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class AnnouncementJpaQueryHandler implements AnnouncementQuery {

    private final AnnouncementRepository announcementRepository;

    private final QAnnouncement announcement = QAnnouncement.announcement;

    @Override
    public List<AnnouncementData> getAnnouncements() {

        BooleanExpression predicate = this.announcement.isNotNull().and(announcement.isDeleted.isFalse());

        List<Announcement> announcements = (List<Announcement>) this.announcementRepository.findAll(predicate);

        return announcements.stream().map(AnnouncementData::new).toList();
    }

    @Override
    public AnnouncementData get(AnnouncementId announcementId) throws HubUserException {

        BooleanExpression predicate = this.announcement.announcementId.eq(announcementId);

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findOne(predicate);

        if (optionalAnnouncement.isEmpty()) {

            throw new HubUserException(HubUserErrors.ANNOUNCEMENT_NOT_FOUND);
        }

        return new AnnouncementData(optionalAnnouncement.get());
    }

}
