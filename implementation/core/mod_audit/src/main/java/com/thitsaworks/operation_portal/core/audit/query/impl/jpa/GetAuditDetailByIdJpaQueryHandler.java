package com.thitsaworks.operation_portal.core.audit.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.data.AuditData;
import com.thitsaworks.operation_portal.core.audit.exception.AuditErrors;
import com.thitsaworks.operation_portal.core.audit.exception.AuditException;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditDetailByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoLogging
@RequiredArgsConstructor
public class GetAuditDetailByIdJpaQueryHandler implements GetAuditDetailByIdQuery {

    private final AuditRepository auditRepository;

    private final QAudit audit = QAudit.audit;

    @Override
    @CoreReadTransactional
    public Output execute(Input input) throws AuditException {

        BooleanExpression predicate = this.audit.auditId.eq(input.auditId());

        var
            audit =
            this.auditRepository.findOne(predicate)
                                .orElseThrow(() -> new AuditException(AuditErrors.AUDIT_NOT_FOUND.format(input.auditId()
                                                                                                              .getId()
                                                                                                              .toString())));

        return new Output(new AuditData(audit));

    }

}
