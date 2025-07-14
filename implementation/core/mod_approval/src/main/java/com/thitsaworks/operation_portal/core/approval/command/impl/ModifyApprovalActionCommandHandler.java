package com.thitsaworks.operation_portal.core.approval.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.approval.command.ModifyApprovalActionCommand;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalErrors;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalException;
import com.thitsaworks.operation_portal.core.approval.model.repository.ApprovalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyApprovalActionCommandHandler implements ModifyApprovalActionCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyApprovalActionCommandHandler.class);

    private final ApprovalRequestRepository approvalRequestRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ApprovalException {

        var
            approvalRequest = this.approvalRequestRepository.findById(input.approvalRequestId())
                                                            .orElseThrow(() -> new ApprovalException(
                                                                ApprovalErrors.APPROVAL_REQUEST_NOT_FOUND));

        approvalRequest.action(input.action());
        approvalRequest.respondedBy(input.respondedBy());

        this.approvalRequestRepository.saveAndFlush(approvalRequest);

        return new Output(approvalRequest.getApprovalRequestId());
    }

}
