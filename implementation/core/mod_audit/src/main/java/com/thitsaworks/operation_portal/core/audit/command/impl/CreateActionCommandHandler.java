package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
//@RequiredArgsConstructor
//public class CreateActionCommandHandler implements CreateActionCommand {
//
//    private static final Logger LOG = LoggerFactory.getLogger(CreateActionCommandHandler.class);
//
//    private final ActionRepository actionRepository;
//
//    @Override
//    @CoreWriteTransactional
//    public Output execute(Input input) {
//
//        Optional<Action> optionalAction =
//                this.actionRepository.findOne(ActionRepository.Filters.withActionName(input.name()));
//
//        if (optionalAction.isEmpty()) {
//
//            Action action = new Action(input.name());
//
//            this.actionRepository.save(action);
//
//            return new CreateActionCommand.Output(true, action.getActionId());
//
//        } else {
//
//            return new CreateActionCommand.Output(false, optionalAction.get().getActionId());
//        }
//    }
//
//}
