package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hubuser.command.RemoveAnnouncementsCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;

import com.thitsaworks.operation_portal.usecase.CoreServicesUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.RemovePastSixMonthsAnnouncements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemovePastSixMonthsAnnouncementsHandler
    extends CoreServicesUseCase<RemovePastSixMonthsAnnouncements.Input, RemovePastSixMonthsAnnouncements.Output>
    implements RemovePastSixMonthsAnnouncements {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN,
                                                                    UserRoleType.OPERATION,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.SUPERUSER);

    private final RemoveAnnouncementsCommand removeAnnouncementsCommand;

    @Autowired
    public RemovePastSixMonthsAnnouncementsHandler(PrincipalCache principalCache,
                                                   RemoveAnnouncementsCommand removeAnnouncementsCommand) {

        super(PERMITTED_ROLES, principalCache);

        this.removeAnnouncementsCommand = removeAnnouncementsCommand;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        RemoveAnnouncementsCommand.Output output = this.removeAnnouncementsCommand.execute(
            new RemoveAnnouncementsCommand.Input());

        return new Output(output.removed());
    }

}
