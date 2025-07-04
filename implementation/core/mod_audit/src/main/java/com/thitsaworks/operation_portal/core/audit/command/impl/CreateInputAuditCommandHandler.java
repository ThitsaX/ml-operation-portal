package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.model.Action;
import com.thitsaworks.operation_portal.core.audit.model.Audit;
import com.thitsaworks.operation_portal.core.audit.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateInputAuditCommandHandler implements CreateInputAuditCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInputAuditCommandHandler.class);

    private final AuditRepository auditRepository;

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        var action = new Action(input.actionName());

        this.actionRepository.save(action);

        Audit audit = new Audit(action.getActionId(), input.actionBy(),
                                input.realmId(), input.inputInfo(), null);

        this.auditRepository.save(audit);

        return new Output(audit.getAuditId());
    }

}
