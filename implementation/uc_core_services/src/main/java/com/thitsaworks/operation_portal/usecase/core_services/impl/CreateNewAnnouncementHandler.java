package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateAnnouncementCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.CoreServicesUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.CreateAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewAnnouncementHandler
    extends CoreServicesUseCase<CreateAnnouncement.Input, CreateAnnouncement.Output>
    implements CreateAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.SUPERUSER,
                                                                    UserRoleType.ADMIN,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.OPERATION);

    private final CreateAnnouncementCommand createAnnouncementCommand;

    public CreateNewAnnouncementHandler(PrincipalCache principalCache,
                                        CreateAnnouncementCommand createAnnouncementCommand) {

        super(PERMITTED_ROLES,
              principalCache);

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
