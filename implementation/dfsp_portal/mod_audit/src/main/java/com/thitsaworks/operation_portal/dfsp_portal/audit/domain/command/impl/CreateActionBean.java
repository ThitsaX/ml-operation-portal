package com.thitsaworks.operation_portal.dfsp_portal.audit.domain.command.impl;

import com.thitsaworks.operation_portal.dfsp_portal.audit.domain.Action;
import com.thitsaworks.operation_portal.dfsp_portal.audit.domain.command.CreateAction;
import com.thitsaworks.operation_portal.dfsp_portal.audit.domain.repository.ActionRepository;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspWriteTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateActionBean implements CreateAction {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionBean.class);

    @Autowired
    ActionRepository actionRepository;

    @Override
    @DfspWriteTransactional
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
