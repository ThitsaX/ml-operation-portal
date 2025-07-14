package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateAnnouncementCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateAnnouncementCommandHandler implements CreateAnnouncementCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementCommandHandler.class);

    private final AnnouncementRepository announcementRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws HubUserException {

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findOne(
                AnnouncementRepository.Filters.findByAnnouncementTitle(input.announcementTitle()));

        if (optionalAnnouncement.isPresent()) {

            throw new HubUserException(HubUserErrors.ALREADY_ANNOUNCED);
        }

        Announcement announcement = new Announcement(input.announcementTitle(),
                                                     input.announcementDetail(),
                                                     input.announcementDate());

        this.announcementRepository.save(announcement);

        return new Output(true);
    }

}
