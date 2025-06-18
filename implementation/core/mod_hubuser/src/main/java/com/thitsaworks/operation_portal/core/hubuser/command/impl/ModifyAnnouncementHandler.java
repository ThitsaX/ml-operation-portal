package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyAnnouncement;
import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.operation_portal.core.hubuser.exception.AnnouncementNotFoundException;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyAnnouncementHandler implements ModifyAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyAnnouncementHandler.class);

    private AnnouncementRepository announcementRepository;

    @Override
    @CoreWriteTransactional
    public ModifyAnnouncement.Output execute(ModifyAnnouncement.Input input)
            throws AnnouncementNotFoundException, AlreadyAnnouncedException {

        Optional<Announcement> optionalAnnouncement = this.announcementRepository.findById(input.announcementId());

        if (optionalAnnouncement.isEmpty()) {
            throw new AnnouncementNotFoundException(input.announcementId().getId().toString());
        }

        Optional<Announcement> optionalAnnouncementTitle = this.announcementRepository.findOne(
                AnnouncementRepository.Filters.findByAnnouncementTitle(input.announcementTitle()));

        Announcement announcement = optionalAnnouncement.get();

        if (optionalAnnouncementTitle.isPresent() &&
                !optionalAnnouncementTitle.get().getAnnouncementId().equals(announcement.getAnnouncementId())) {
            throw new AlreadyAnnouncedException(input.announcementTitle());
        }

        this.announcementRepository.save(announcement.announcementTitle(input.announcementTitle())
                                                     .announcementDetail(input.announcementDetail())
                                                     .announcementDate(input.announcementDate())
                                                     .isDeleted(input.isDeleted()));

        return new ModifyAnnouncement.Output(input.announcementId(), true);
    }

}
