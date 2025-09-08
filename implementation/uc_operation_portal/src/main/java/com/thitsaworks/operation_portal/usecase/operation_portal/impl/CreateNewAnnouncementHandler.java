package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.participant.command.CreateAnnouncementCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateAnnouncement;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateNewAnnouncementHandler
    extends OperationPortalUseCase<CreateAnnouncement.Input, CreateAnnouncement.Output>
    implements CreateAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementHandler.class);

    private final CreateAnnouncementCommand createAnnouncementCommand;

    public CreateNewAnnouncementHandler(PrincipalCache principalCache,
                                        CreateAnnouncementCommand createAnnouncementCommand,
                                        ActionAuthorizationManager actionAuthorizationManager) {

        super(principalCache, actionAuthorizationManager);

        this.createAnnouncementCommand = createAnnouncementCommand;

    }

    @Override
    public CreateAnnouncement.Output onExecute(CreateAnnouncement.Input input) throws DomainException {

        CreateAnnouncementCommand.Output output =
            this.createAnnouncementCommand.execute(
                new CreateAnnouncementCommand.Input(input.announcementTitle(),
                                                    input.announcementDetail(),
                                                    input.announcementDate()));

        return new CreateAnnouncement.Output(output.created());
    }

}
