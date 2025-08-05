package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantRoleActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantRoleActions;
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
public class GrantRoleActionsHandler
    extends OperationPortalAuditableUseCase<GrantRoleActions.Input, GrantRoleActions.Output>
    implements GrantRoleActions {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final GrantRoleActionCommand grantRoleActionCommand;

    private final PlatformTransactionManager transactionManager;

    public GrantRoleActionsHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   GrantRoleActionCommand grantRoleActionsCommand,
                                   @Qualifier(PersistenceQualifiers.Core.TRANSACTION_MANAGER)
                                   PlatformTransactionManager transactionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.grantRoleActionCommand = grantRoleActionsCommand;
        this.transactionManager = transactionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = this.transactionManager.getTransaction(def);

        try {

            for (var singleRoleGrant : input.singleRoleGrantList()) {

                for (var action : singleRoleGrant.actionList()) {
                    this.grantRoleActionCommand.execute(new GrantRoleActionCommand.Input(singleRoleGrant.role(),
                                                                                         action));

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
