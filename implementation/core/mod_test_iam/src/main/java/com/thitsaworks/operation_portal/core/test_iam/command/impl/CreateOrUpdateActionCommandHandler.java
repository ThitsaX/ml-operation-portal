package com.thitsaworks.operation_portal.core.test_iam.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.test_iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository.Filters.withActionCode;

@Service
@RequiredArgsConstructor
public class CreateOrUpdateActionCommandHandler implements CreateOrUpdateActionCommand {

    private  final IAMActionRepository IAMActionRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        Optional<IAMAction> optAction =
            this.IAMActionRepository.findOne(withActionCode(input.actionCode()));

        IAMAction IAMAction;

        if (optAction.isPresent()) {
            IAMAction = optAction.get();
            IAMAction.scope(input.scope()).description(input.description());
        } else {
            IAMAction = new IAMAction(input.actionCode(), input.scope(), input.description());
        }

        this.IAMActionRepository.save(IAMAction);

        return new Output(IAMAction.getActionId());
    }

}
