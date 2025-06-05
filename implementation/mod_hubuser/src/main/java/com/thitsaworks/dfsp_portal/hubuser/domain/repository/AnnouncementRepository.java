package com.thitsaworks.dfsp_portal.hubuser.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.dfsp_portal.hubuser.domain.Announcement;
import com.thitsaworks.dfsp_portal.hubuser.domain.QAnnouncement;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AnnouncementRepository extends JpaRepository<Announcement, AnnouncementId>,
        QuerydslPredicateExecutor<Announcement> {

    public static BooleanExpression findByAnnouncementTitle(String announcementTitle) {

        return QAnnouncement.announcement.announcementTitle.eq(announcementTitle);
    }

}
