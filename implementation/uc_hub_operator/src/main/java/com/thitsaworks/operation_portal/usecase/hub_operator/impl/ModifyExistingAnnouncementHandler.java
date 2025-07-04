package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hubuser.command.ModifyAnnouncement;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyExistingAnnouncement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyExistingAnnouncementHandler
        extends HubOperatorAuditableUseCase<ModifyExistingAnnouncement.Input, ModifyExistingAnnouncement.Output>
        implements ModifyExistingAnnouncement {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingAnnouncementHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final ModifyAnnouncement modifyAnnouncement;

    private final ObjectMapper objectMapper;

    @Autowired
    public ModifyExistingAnnouncementHandler(CreateInputAuditCommand createInputAuditCommand,
                                             CreateOutputAuditCommand createOutputAuditCommand,
                                             CreateExceptionAuditCommand createExceptionAuditCommand,
                                             ModifyAnnouncement modifyAnnouncement,
                                             ObjectMapper objectMapper,
                                             PrincipalCache principalCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyAnnouncement = modifyAnnouncement;
        this.objectMapper = objectMapper;
    }

    @Override
    public ModifyExistingAnnouncement.Output onExecute(ModifyExistingAnnouncement.Input input) throws
                                                                                               OperationPortalException {

        ModifyAnnouncement.Output output = this.modifyAnnouncement.execute(
                new ModifyAnnouncement.Input(input.announcementId(), input.announcementTitle(),
                                             input.announcementDetail(), input.announcementDate(), input.isDeleted()));

        return new ModifyExistingAnnouncement.Output(output.announcementId(), output.modified());
    }

}
