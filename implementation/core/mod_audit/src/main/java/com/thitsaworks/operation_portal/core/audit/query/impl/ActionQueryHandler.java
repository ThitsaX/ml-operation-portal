package com.thitsaworks.operation_portal.core.audit.query.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.data.ActionData;
import com.thitsaworks.operation_portal.core.audit.model.QAction;
import com.thitsaworks.operation_portal.core.audit.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.audit.query.ActionQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class ActionQueryHandler implements ActionQuery {

    private final ActionRepository actionRepository;

    private final QAction action = QAction.action;

    @Override
    public List<ActionData> getAction() {

        return this.actionRepository.findAll()
                                    .stream()
                                    .map(Action -> new ActionData(Action.getActionId(),
                                                                  Action.getName(),
                                                                  Action.getCreatedAt()))
                                    .toList();
    }

    @Override
    public Optional<ActionData> get(String Name) {

        BooleanExpression predicate = this.action.name.eq(Name);

        return this.actionRepository.findOne(predicate)
                                    .map(Action -> new ActionData(Action.getActionId(),
                                                                  Action.getName(),
                                                                  Action.getCreatedAt()));

    }

}