package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.fspiop.model.Currency;
import com.thitsaworks.operation_portal.component.fspiop.model.Extension;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TransferIdGenerator;
import com.thitsaworks.operation_portal.core.approval.command.ModifyApprovalActionCommand;
import com.thitsaworks.operation_portal.core.approval.query.ApprovalRequestQuery;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.HubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyApprovalAction;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import com.thitsaworks.operation_portal.usecase.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.EnumSet;
import java.util.Set;

@Service
public class ModifyApprovalActionHandler
    extends OperationPortalAuditableUseCase<ModifyApprovalAction.Input, ModifyApprovalAction.Output>
    implements ModifyApprovalAction {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyApprovalActionHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ModifyApprovalActionCommand modifyApprovalActionCommand;

    private final ApprovalRequestQuery approvalRequestQuery;

    private final HubClient hubClient;

    private final Utility utility;

    public ModifyApprovalActionHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       ModifyApprovalActionCommand modifyApprovalActionCommand,
                                       ApprovalRequestQuery approvalRequestQuery,
                                       HubClient hubClient,
                                       Utility utility) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyApprovalActionCommand = modifyApprovalActionCommand;
        this.approvalRequestQuery = approvalRequestQuery;
        this.hubClient = hubClient;
        this.utility = utility;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var approvalRequestData = this.approvalRequestQuery.getPendingApprovalRequestByID(input.approvalRequestId());

                String action = "Deposit".equalsIgnoreCase(approvalRequestData.requestedAction()) ? "recordFundsIn" :
                    "Withdraw".equalsIgnoreCase(approvalRequestData.requestedAction()) ? "recordFundsOutPrepareReserve" :
                        approvalRequestData.requestedAction();



        Money
            money =
            new Money().currency(Currency.valueOf(approvalRequestData.currency()))
                       .amount(approvalRequestData.amount()
                                                  .toString());

        ExtensionList extensionList= new ExtensionList();
        Extension extension= new Extension();
        extension.setKey("requestUser");
        extension.setValue(this.utility.getEmail(new HubUserId(approvalRequestData.requestedBy().getId())));
        extension.setKey("approveUser");
        extension.setValue(this.utility.getEmail(new HubUserId(approvalRequestData.requestedBy().getId())));
        extensionList.addExtensionItem(extension);




        PostParticipantBalance.Request
            request =
            new PostParticipantBalance.Request(TransferIdGenerator.generateTransferId(),
                                               this.utility.getEmail(new HubUserId(approvalRequestData.requestedBy()
                                                                                          .getId())),
                                               action,
                                               "Admin portal funds in request",
                                               money,
                                               extensionList
                                               );

        PostParticipantBalance.Response
            response =
            this.hubClient.postParticipantBalance(approvalRequestData.participantId(),
                                                  approvalRequestData.participantCurrencyId(),
                                                  request);

        var
            output =
            this.modifyApprovalActionCommand.execute(new ModifyApprovalActionCommand.Input(input.approvalRequestId(),
                                                                                           input.action(),
                                                                                           input.responseUserId()));

        return new Output(output.approvalRequestId());
    }

}
