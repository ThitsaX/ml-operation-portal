package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateAuditCommand;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAuditCommandHandler implements CreateAuditCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditCommandHandler.class);

    private final AuditRepository auditRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

//        CreateActionCommand.Output action = this.createActionCommand.execute(new CreateActionCommand.Input(input.actionName()));
//
//        Audit audit = new Audit(action.actionId(), input.actionBy(),
//                                input.realmId(), input.inputInfo(), input.outputInfo());
//
//        this.auditRepository.save(audit);

        return new CreateAuditCommand.Output(true);
    }

}
