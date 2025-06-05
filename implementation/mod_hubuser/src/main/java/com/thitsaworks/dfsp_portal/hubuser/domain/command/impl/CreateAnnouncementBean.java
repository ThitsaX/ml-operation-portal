package com.thitsaworks.dfsp_portal.hubuser.domain.command.impl;

import com.thitsaworks.dfsp_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.dfsp_portal.hubuser.domain.Announcement;
import com.thitsaworks.dfsp_portal.hubuser.domain.command.CreateAnnouncement;
import com.thitsaworks.dfsp_portal.hubuser.domain.repository.AnnouncementRepository;
import com.thitsaworks.dfsp_portal.hubuser.exception.AlreadyAnnouncedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CreateAnnouncementBean implements CreateAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementBean.class);

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws AlreadyAnnouncedException {

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findOne(
                AnnouncementRepository.findByAnnouncementTitle(input.getAnnouncementTitle()));

        if (optionalAnnouncement.isPresent()) {
            throw new AlreadyAnnouncedException(input.getAnnouncementTitle());
        }

        Announcement announcement = new Announcement(input.getAnnouncementTitle(), input.getAnnouncementDetail(), input.getAnnouncementDate());

        this.announcementRepository.save(announcement);

        return new Output(true);
    }

}
