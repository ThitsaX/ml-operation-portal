package com.thitsaworks.operation_portal.core.hubuser.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.QAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AnnouncementRepository extends JpaRepository<Announcement, AnnouncementId>,
        QuerydslPredicateExecutor<Announcement> {

    class Filters {

        private static final QAnnouncement announcement = QAnnouncement.announcement;

        public static BooleanExpression findByAnnouncementTitle(String announcementTitle) {

            return announcement.announcementTitle.eq(announcementTitle);
        }

    }

}
