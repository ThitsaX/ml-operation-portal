package com.thitsaworks.operation_portal.core.test_iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.QIAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import com.thitsaworks.operation_portal.core.test_iam.query.ActionQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionJpaQueryHandler implements ActionQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ActionJpaQueryHandler.class);

    private final QIAMAction iamAction = QIAMAction.iAMAction;

    private final IAMActionRepository iamActionRepository;

    @Override
    public ActionData get(ActionCode actionCode) throws IAMException {

        BooleanExpression predicate = this.iamAction.actionCode.eq(actionCode);

        var action = this.iamActionRepository.findOne(predicate);

        if (action.isEmpty()) {
            throw new IAMException(IAMErrors.ACTION_NOT_FOUND);
        }

        return new ActionData(action.get());
    }

}
