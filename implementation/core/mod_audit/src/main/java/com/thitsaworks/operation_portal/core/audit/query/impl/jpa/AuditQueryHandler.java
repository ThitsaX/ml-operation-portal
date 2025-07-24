package com.thitsaworks.operation_portal.core.audit.query.impl.jpa;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.audit.data.AuditData;
import com.thitsaworks.operation_portal.core.audit.model.QAudit;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import com.thitsaworks.operation_portal.core.audit.query.AuditQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class AuditQueryHandler implements AuditQuery {

    private final AuditRepository auditRepository;

    private final QAudit audit = QAudit.audit;

    @Override
    public List<AuditData> getAudits() {



        return this.auditRepository.findAll()
                                   .stream()
                                   .map(Audit -> new AuditData(Audit.getAuditId(),
                                                               Audit.getActionId(),
                                                               Audit.getUserId(),
                                                               Audit.getRealmId(),
                                                               Audit.getInputInfo(),
                                                               Audit.getOutputInfo()))
                                   .toList();
    }

    @Override
    public Optional<AuditData> get(String Name) {

        return Optional.empty();
    }

}
