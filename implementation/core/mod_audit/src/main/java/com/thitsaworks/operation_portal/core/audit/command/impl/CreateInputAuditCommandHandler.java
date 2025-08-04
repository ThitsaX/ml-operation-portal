package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.model.Audit;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateInputAuditCommandHandler implements CreateInputAuditCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInputAuditCommandHandler.class);

    private final AuditRepository auditRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

//        Optional<Action> optionalAction =
//                this.actionRepository.findOne(ActionRepository.Filters.withActionName(input.actionName()));
//
//        ActionId actionId;
//
//        if (optionalAction.isEmpty()) {
//
//            Action action = new Action(input.actionName());
//
//            this.actionRepository.save(action);
//
//            actionId = action.getActionId();
//
//        } else {
//
//            actionId = optionalAction.get().getActionId();
//        }
//
//        Audit audit = new Audit(actionId, input.actionBy(),
//                                input.realmId(), input.inputInfo(), null);
//
//        this.auditRepository.save(audit);
//
//        return new Output(audit.getAuditId());

        return null;
    }

}
