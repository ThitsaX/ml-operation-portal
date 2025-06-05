package com.thitsaworks.dfsp_portal.hubuser.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.hubuser.domain.Announcement;
import com.thitsaworks.dfsp_portal.hubuser.domain.command.ModifyAnnouncement;
import com.thitsaworks.dfsp_portal.hubuser.domain.repository.AnnouncementRepository;
import com.thitsaworks.dfsp_portal.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.dfsp_portal.hubuser.exception.AnnouncementNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyAnnouncementBean implements ModifyAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyAnnouncementBean.class);

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    @WriteTransactional
    public ModifyAnnouncement.Output execute(ModifyAnnouncement.Input input)
            throws AnnouncementNotFoundException, AlreadyAnnouncedException {

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findById(input.getAnnouncementId());

        if (optionalAnnouncement.isEmpty()) {
            throw new AnnouncementNotFoundException(input.getAnnouncementId().getId().toString());
        }

        Optional<Announcement> optionalAnnouncementTitle = this.announcementRepository.findOne(
                AnnouncementRepository.findByAnnouncementTitle(input.getAnnouncementTitle()));

        Announcement announcement = optionalAnnouncement.get();

        if (optionalAnnouncementTitle.isPresent() &&
                !optionalAnnouncementTitle.get().getAnnouncementId().equals(announcement.getAnnouncementId())) {
            throw new AlreadyAnnouncedException(input.getAnnouncementTitle());
        }

        this.announcementRepository.save(announcement.announcementTitle(input.getAnnouncementTitle())
                                                     .announcementDetail(input.getAnnouncementDetail())
                .announcementDate(input.getAnnouncement_date())
                                                     .isDeleted(input.isDeleted()));

        return new ModifyAnnouncement.Output(input.getAnnouncementId(), true);
    }

}
