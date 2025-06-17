package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.core.audit.model.Audit;
import com.thitsaworks.operation_portal.core.audit.command.CreateAction;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAuditBean implements CreateAudit {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditBean.class);

    private final AuditRepository auditRepository;

    private final CreateAction createAction;

    @Override
    @DfspWriteTransactional
    public Output execute(Input input) throws UserNotFoundException {

        CreateAction.Output action = this.createAction.execute(new CreateAction.Input(input.getActionName()));

        Audit audit = new Audit(action.getActionId(), input.getActionBy(),
                input.getRealmId(), input.getInputInfo(), input.getOutputInfo());

        this.auditRepository.save(audit);

        return new CreateAudit.Output(true);
    }

}
