package com.thitsaworks.operation_portal.core.approval.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.approval.command.CreateApprovalRequestCommand;
import com.thitsaworks.operation_portal.core.approval.model.ApprovalRequest;
import com.thitsaworks.operation_portal.core.approval.model.repository.ApprovalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateApprovalRequestCommandHandler implements CreateApprovalRequestCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateApprovalRequestCommandHandler.class);

    private final ApprovalRequestRepository approvalRequestRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        var approvalRequest = new ApprovalRequest(input.requestedAction(),
                                                  input.participant(),
                                                  input.participantCurrency(),
                                                  input.participantSettlementCurrencyId(),
                                                  input.participantPositionCurrencyId(),
                                                  input.amount(),
                                                  input.requestedBy());

        this.approvalRequestRepository.save(approvalRequest);

        return new Output(approvalRequest.getApprovalRequestId());
    }

}
