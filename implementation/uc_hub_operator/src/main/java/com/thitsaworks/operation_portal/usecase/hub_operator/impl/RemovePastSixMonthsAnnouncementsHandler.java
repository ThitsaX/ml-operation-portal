package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncements;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.HubOperatorUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.RemovePastSixMonthsAnnouncements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemovePastSixMonthsAnnouncementsHandler
        extends HubOperatorUseCase<RemovePastSixMonthsAnnouncements.Input, RemovePastSixMonthsAnnouncements.Output>
        implements RemovePastSixMonthsAnnouncements {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);
    private final RemoveAnnouncements removeAnnouncements;

    @Autowired
    public RemovePastSixMonthsAnnouncementsHandler(
            PrincipalCache principalCache,
            RemoveAnnouncements removeAnnouncements) {

        super(
                PERMITTED_ROLES,
                principalCache);

        this.removeAnnouncements = removeAnnouncements;
    }

    @Override
    public Output onExecute(Input input) throws OperationPortalException {

        RemoveAnnouncements.Output output = this.removeAnnouncements.execute(
                new RemoveAnnouncements.Input());

        return new Output(output.removed());
    }

//
//    @Override
//    protected String getName() {
//
//        return RemovePastSixMonthsAnnouncements.class.getCanonicalName();
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
//        return RemovePastSixMonthsAnnouncements.class.getName();
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

}
