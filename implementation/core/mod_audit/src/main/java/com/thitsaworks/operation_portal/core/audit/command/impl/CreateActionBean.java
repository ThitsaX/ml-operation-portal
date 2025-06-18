package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateAction;
import com.thitsaworks.operation_portal.core.audit.model.Action;
import com.thitsaworks.operation_portal.core.audit.model.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateActionBean implements CreateAction {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionBean.class);

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        Optional<Action> optionalAction =
                this.actionRepository.findOne(ActionRepository.Filters.withActionName(input.getName()));

        if (optionalAction.isEmpty()) {

            Action action = new Action(input.getName());

            this.actionRepository.save(action);

            return new CreateAction.Output(true, action.getActionId());

        } else {

            return new CreateAction.Output(false, optionalAction.get().getActionId());
        }
    }

}
