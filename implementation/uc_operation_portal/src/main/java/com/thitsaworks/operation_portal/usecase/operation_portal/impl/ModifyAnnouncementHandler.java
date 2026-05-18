package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyAnnouncementCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyAnnouncement;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ActionMetadata(category = ActionCategory.ANNOUNCEMENT_AND_GREETING_CONTENT)
public class ModifyAnnouncementHandler
    extends OperationPortalUseCase<ModifyAnnouncement.Input, ModifyAnnouncement.Output>
    implements ModifyAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(
        ModifyAnnouncementHandler.class);

    private final ModifyAnnouncementCommand modifyAnnouncementCommand;

    @Autowired
    public ModifyAnnouncementHandler(ModifyAnnouncementCommand modifyAnnouncementCommand,
                                     PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager) {

        super(principalCache, actionAuthorizationManager);

        this.modifyAnnouncementCommand = modifyAnnouncementCommand;
    }

    @Override
    public ModifyAnnouncement.Output onExecute(ModifyAnnouncement.Input input)
        throws DomainException {

        ModifyAnnouncementCommand.Output output = this.modifyAnnouncementCommand.execute(
            new ModifyAnnouncementCommand.Input(
                input.announcementId(), input.announcementTitle(), input.announcementDetail(),
                input.announcementDate(), input.isDeleted()));

        return new ModifyAnnouncement.Output(output.announcementId(), output.modified());
    }

}
