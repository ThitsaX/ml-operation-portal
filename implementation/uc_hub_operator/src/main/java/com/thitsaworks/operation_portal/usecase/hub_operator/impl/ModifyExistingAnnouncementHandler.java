package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
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

//    @Override
//    protected String getName() {
//
//        return ModifyExistingAnnouncement.class.getCanonicalName();
//    }
//
//    @Override
//    protected String getDescription() {
//
//        return null;
//    }
//
//    @Override
//    protected String getScope() {
//
//        return "uc_hub_operator";
//    }
//
//    @Override
//    protected String getId() {
//
//        return ModifyExistingAnnouncement.class.getName();
//    }
//
//    @Override
//    public boolean isOwned(Object userDetails) {
//
//        return true;
//    }
//
//    @Override
//    public boolean isAuthorized(Object userDetails) {
//
//        return true;
//    }
//
//    @Override
//    public void onAudit(ModifyExistingAnnouncement.Input input, ModifyExistingAnnouncement.Output output)
//            throws UserNotFoundException {
//
//        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();
//
//        Auditor.audit(this.objectMapper, ModifyExistingAnnouncement.class, input, output,
//                      new UserId(securityContext.userId()),
//                      securityContext.realmId() == null ? null : new RealmId(securityContext.realmId()));
//    }

}
