package com.thitsaworks.operation_portal.core.audit.query.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.core.audit.data.UserData;
import com.thitsaworks.operation_portal.core.audit.model.Audit;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import com.thitsaworks.operation_portal.core.audit.query.GetActionByQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetActionByQueryHandler implements GetActionByQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionByQueryHandler.class);

    private final QAudit audit = QAudit.audit;

    private final AuditRepository auditRepository;

    @Override
    public Output execute(Input input) throws Exception {

        BooleanExpression predicate = this.audit.realmId.id.eq(input.userId().getId());

        Optional<Audit> optionalAudit = this.auditRepository.findOne(predicate);

        return optionalAudit.map(value -> new Output(new UserData(value.getUserId().getId(),
                                                                  value.getRealmId().getId()))).orElse(null);

    }

}
