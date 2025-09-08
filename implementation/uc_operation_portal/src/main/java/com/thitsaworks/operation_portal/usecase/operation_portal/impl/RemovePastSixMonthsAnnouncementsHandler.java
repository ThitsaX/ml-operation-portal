package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.participant.command.RemoveAnnouncementsCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemovePastSixMonthsAnnouncements;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemovePastSixMonthsAnnouncementsHandler
    extends OperationPortalUseCase<RemovePastSixMonthsAnnouncements.Input, RemovePastSixMonthsAnnouncements.Output>
    implements RemovePastSixMonthsAnnouncements {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsHandler.class);

    private final RemoveAnnouncementsCommand removeAnnouncementsCommand;

    @Autowired
    public RemovePastSixMonthsAnnouncementsHandler(PrincipalCache principalCache,
                                                   ActionAuthorizationManager actionAuthorizationManager,
                                                   RemoveAnnouncementsCommand removeAnnouncementsCommand) {

        super(principalCache, actionAuthorizationManager);

        this.removeAnnouncementsCommand = removeAnnouncementsCommand;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        RemoveAnnouncementsCommand.Output output = this.removeAnnouncementsCommand.execute(
            new RemoveAnnouncementsCommand.Input());

        return new Output(output.removed());
    }

}
