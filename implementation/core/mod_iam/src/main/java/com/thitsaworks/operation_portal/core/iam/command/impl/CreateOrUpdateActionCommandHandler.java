package com.thitsaworks.operation_portal.core.iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateOrUpdateActionCommandHandler implements CreateOrUpdateActionCommand {

    private final ActionRepository actionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        Optional<Action> optAction =
            this.actionRepository.findOne(ActionRepository.Filters.withActionCode(input.actionCode()));

        Action action;

        if (optAction.isPresent()) {

            action = optAction.get();
            action.scope(input.scope())
                  .description(input.description());

        } else {

            action = new Action(input.actionCode(), input.scope(), input.description());
        }

        this.actionRepository.save(action);

        return new Output(action.getActionId());
    }

}
