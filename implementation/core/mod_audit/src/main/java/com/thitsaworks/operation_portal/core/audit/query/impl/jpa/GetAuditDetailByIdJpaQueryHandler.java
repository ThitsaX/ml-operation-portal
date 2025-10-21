package com.thitsaworks.operation_portal.core.audit.query.impl.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.query.GetAuditDetailByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAuditDetailByIdJpaQueryHandler implements GetAuditDetailByIdQuery {

    private final JPAQueryFactory readQueryFactory;

    @Override
    @CoreReadTransactional
    public Output execute(Input input) {

        QAudit audit = QAudit.audit;

        JPAQuery<Tuple> tupleSQLQuery = this.readQueryFactory.select(audit.auditId, audit.inputInfo, audit.outputInfo)
                                                             .from(audit)
                                                             .where(audit.auditId.id.eq(input.auditId()
                                                                                             .getEntityId()));

        Tuple tuple = tupleSQLQuery.fetchOne();

        assert tuple != null;
        return new Output(tuple.get(audit.auditId),
                          tuple.get(audit.inputInfo),
                          tuple.get(audit.outputInfo));
    }

}
