package com.thitsaworks.operation_portal.audit.domain.command.impl;

import com.thitsaworks.operation_portal.audit.domain.Audit;
import com.thitsaworks.operation_portal.audit.domain.command.CreateAction;
import com.thitsaworks.operation_portal.audit.domain.command.CreateAudit;
import com.thitsaworks.operation_portal.audit.domain.repository.AuditRepository;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAuditBean implements CreateAudit {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditBean.class);

    @Autowired
    AuditRepository auditRepository;

    @Autowired
    CreateAction createAction;

    @Override
    @WriteTransactional
    public Output execute(Input input) throws UserNotFoundException {

        CreateAction.Output action = this.createAction.execute(new CreateAction.Input(input.getActionName()));

        Audit audit = new Audit(action.getActionId(), input.getActionBy(),
                input.getRealmId(), input.getInputInfo(), input.getOutputInfo());

        this.auditRepository.save(audit);

        return new CreateAudit.Output(true);
    }

}
