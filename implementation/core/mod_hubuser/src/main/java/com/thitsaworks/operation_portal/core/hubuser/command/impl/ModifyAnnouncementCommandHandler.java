package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyAnnouncementCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyAnnouncementCommandHandler implements ModifyAnnouncementCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyAnnouncementCommandHandler.class);

    private AnnouncementRepository announcementRepository;

    @Override
    @CoreWriteTransactional
    public ModifyAnnouncementCommand.Output execute(ModifyAnnouncementCommand.Input input)
            throws HubUserException {

        Announcement announcement =
                this.announcementRepository.findById(input.announcementId()).orElseThrow(() -> new HubUserException(
                        HubUserErrors.ANNOUNCEMENT_NOT_FOUND));

        Optional<Announcement> optionalAnnouncementTitle = this.announcementRepository.findOne(
                AnnouncementRepository.Filters.findByAnnouncementTitle(input.announcementTitle()));

        if (optionalAnnouncementTitle.isPresent() &&
                !optionalAnnouncementTitle.get().getAnnouncementId().equals(announcement.getAnnouncementId())) {
            throw new HubUserException(HubUserErrors.ALREADY_ANNOUNCED);
        }

        this.announcementRepository.save(announcement.announcementTitle(input.announcementTitle())
                                                     .announcementDetail(input.announcementDetail())
                                                     .announcementDate(input.announcementDate())
                                                     .isDeleted(input.isDeleted()));

        return new ModifyAnnouncementCommand.Output(input.announcementId(), true);
    }

}
