package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransferDetailQuery;
import com.thitsaworks.operation_portal.usecase.CentralLedgerAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetTransferDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetTransferDetailsHandler
    extends CentralLedgerAuditableUseCase<GetTransferDetails.Input, GetTransferDetails.Output>
    implements GetTransferDetails {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetTransferDetailQuery getTransferDetailQuery;

    public GetTransferDetailsHandler(CreateInputAuditCommand createInputAuditCommand,
                                     CreateOutputAuditCommand createOutputAuditCommand,
                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache,
                                     GetTransferDetailQuery getTransferDetailQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.getTransferDetailQuery = getTransferDetailQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetTransferDetailQuery.Output output = this.getTransferDetailQuery.execute(new GetTransferDetailQuery.Input(
            input.transferId()));

        return new Output(output.getBusinessData());
    }

}
