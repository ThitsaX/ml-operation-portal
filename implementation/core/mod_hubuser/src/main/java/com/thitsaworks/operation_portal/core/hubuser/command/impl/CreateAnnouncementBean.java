package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateAnnouncement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateAnnouncementBean implements CreateAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementBean.class);

    private final AnnouncementRepository announcementRepository;

    @Override
    @DfspWriteTransactional
    public Output execute(Input input) throws AlreadyAnnouncedException {

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findOne(
                AnnouncementRepository.Filters.findByAnnouncementTitle(input.getAnnouncementTitle()));

        if (optionalAnnouncement.isPresent()) {
            throw new AlreadyAnnouncedException(input.getAnnouncementTitle());
        }

        Announcement announcement = new Announcement(input.getAnnouncementTitle(), input.getAnnouncementDetail(), input.getAnnouncementDate());

        this.announcementRepository.save(announcement);

        return new Output(true);
    }

}
