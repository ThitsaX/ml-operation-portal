package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantMenuActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantMenuActions;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.net.ConnectException;
import java.util.Set;

@Service
public class GrantMenuActionsHandler
    extends OperationPortalAuditableUseCase<GrantMenuActions.Input, GrantMenuActions.Output>
    implements GrantMenuActions {

    private static final Logger LOG = LoggerFactory.getLogger(GrantMenuActionsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN, UserRoleType.OPERATION);

    private final GrantMenuActionCommand grantMenuActionCommand;

    private final PlatformTransactionManager transactionManager;

    public GrantMenuActionsHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   GrantMenuActionCommand grantMenuActionCommand,
                                   @Qualifier(PersistenceQualifiers.Core.TRANSACTION_MANAGER)
                                   PlatformTransactionManager transactionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.grantMenuActionCommand = grantMenuActionCommand;
        this.transactionManager = transactionManager;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = this.transactionManager.getTransaction(def);

        try {

            for (var menu : input.singleMenuGrantList()) {

                for (var action : menu.actionList()) {
                    this.grantMenuActionCommand.execute(new GrantMenuActionCommand.Input(menu.menuName(), action));
                }
            }

            this.transactionManager.commit(status);

        } catch (Exception e) {

            this.transactionManager.rollback(status);
            throw e;
        }

        return new Output(true);
    }

}
