package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.QAction;
import com.thitsaworks.operation_portal.core.iam.model.repository.ActionRepository;
import com.thitsaworks.operation_portal.core.iam.query.ActionQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionJpaQueryHandler implements ActionQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ActionJpaQueryHandler.class);

    private final QAction iamAction = QAction.action;

    private final ActionRepository actionRepository;

    @Override
    public ActionData get(ActionCode actionCode) throws IAMException {

        BooleanExpression predicate = this.iamAction.actionCode.eq(actionCode);

        var action = this.actionRepository.findOne(predicate);

        if (action.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        return new ActionData(action.get());
    }

}
