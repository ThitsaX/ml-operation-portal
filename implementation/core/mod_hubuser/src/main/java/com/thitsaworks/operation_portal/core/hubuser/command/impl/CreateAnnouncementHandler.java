package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateAnnouncement;
import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateAnnouncementHandler implements CreateAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementHandler.class);

    private final AnnouncementRepository announcementRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws AlreadyAnnouncedException {

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findOne(
                AnnouncementRepository.Filters.findByAnnouncementTitle(input.announcementTitle()));

        if (optionalAnnouncement.isPresent()) {
            throw new AlreadyAnnouncedException(input.announcementTitle());
        }

        Announcement announcement = new Announcement(input.announcementTitle(),
                                                     input.announcementDetail(),
                                                     input.announcementDate());

        this.announcementRepository.save(announcement);

        return new Output(true);
    }

}
