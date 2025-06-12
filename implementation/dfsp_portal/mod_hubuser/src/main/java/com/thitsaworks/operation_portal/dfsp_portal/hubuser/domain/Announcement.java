package com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain;

import com.thitsaworks.operation_portal.component.data.jpa.JpaInstantConverter;
import com.thitsaworks.operation_portal.component.misc.data.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.identity.AnnouncementId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.time.Instant;

@Entity
@Table(name = "tbl_announcement")
@Getter
@NoArgsConstructor
public class Announcement extends JpaEntity<AnnouncementId> {

    @EmbeddedId
    protected AnnouncementId announcementId;

    @Column(name = "announcement_title")
    protected String announcementTitle;

    @Column(name = "announcement_detail")
    protected String announcementDetail;

    @Column(name = "is_deleted")
    protected boolean isDeleted;

    @Column(name = "announcement_date")
    @Convert(converter = JpaInstantConverter.class)
    protected Instant announcementDate;

    public Announcement(String announcementTitle, String announcementDetail, Instant announcementDate) {

        Validate.notBlank(announcementTitle);
        Validate.notBlank(announcementDetail);
        Validate.notNull(announcementDate);

        this.announcementId = new AnnouncementId(Snowflake.get().nextId());
        this.announcementTitle = announcementTitle;
        this.announcementDetail = announcementDetail;
        this.announcementDate = announcementDate;
        this.isDeleted = false;
    }

    @Override
    protected AnnouncementId getPrimaryId() {

        return this.announcementId;
    }

    public Announcement announcementTitle(String announcementTitle) {

        this.announcementTitle = announcementTitle;
        return this;

    }

    public Announcement announcementDetail(String announcementDetail) {

        this.announcementDetail = announcementDetail;
        return this;

    }

    public Announcement announcementDate(Instant announcementDate) {

        this.announcementDate = announcementDate;
        return this;

    }

    public Announcement isDeleted(boolean isDeleted) {

        this.isDeleted = isDeleted;
        return this;

    }

}
