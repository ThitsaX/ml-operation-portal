/*
 * Copyright (c) 2024-2026 ThitsaWorks Pte. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyNdcThresholdApprovalAction;
import com.thitsaworks.operation_portal.usecase.operation_portal.approval.NdcThresholdApprovalService;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

@Service
@ActionMetadata(category = ActionCategory.APPROVAL_WORKFLOW)
public class ModifyNdcThresholdApprovalActionHandler
    extends OperationPortalAuditableUseCase<ModifyNdcThresholdApprovalAction.Input,
                                             ModifyNdcThresholdApprovalAction.Output>
    implements ModifyNdcThresholdApprovalAction {

    private final NdcThresholdApprovalService approvalService;

    public ModifyNdcThresholdApprovalActionHandler(
        CreateInputAuditCommand createInputAuditCommand,
        CreateOutputAuditCommand createOutputAuditCommand,
        CreateExceptionAuditCommand createExceptionAuditCommand,
        ObjectMapper objectMapper,
        PrincipalCache principalCache,
        ActionAuthorizationManager actionAuthorizationManager,
        NdcThresholdApprovalService approvalService) {

        super(createInputAuditCommand, createOutputAuditCommand, createExceptionAuditCommand,
              objectMapper, principalCache, actionAuthorizationManager);
        this.approvalService = approvalService;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var approvalRequestId = this.approvalService.decide(
            input.approvalRequestId(),
            input.action(),
            input.respondedBy());

        return new Output(approvalRequestId, input.action().name());
    }
}
