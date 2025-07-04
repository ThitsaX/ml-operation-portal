package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateAction;
import com.thitsaworks.operation_portal.core.audit.command.CreateAudit;
import com.thitsaworks.operation_portal.core.audit.model.Audit;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAuditBean implements CreateAudit {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditBean.class);

    private final AuditRepository auditRepository;

    private final CreateAction createAction;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        CreateAction.Output action = this.createAction.execute(new CreateAction.Input(input.actionName()));

        Audit audit = new Audit(action.actionId(), input.actionBy(),
                                input.realmId(), input.inputInfo(), input.outputInfo());

        this.auditRepository.save(audit);

        return new CreateAudit.Output(true);
    }

}
