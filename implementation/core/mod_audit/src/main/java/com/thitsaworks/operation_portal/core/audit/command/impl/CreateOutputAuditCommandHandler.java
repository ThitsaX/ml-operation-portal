package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.exception.AuditErrors;
import com.thitsaworks.operation_portal.core.audit.exception.AuditException;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOutputAuditCommandHandler implements CreateOutputAuditCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateOutputAuditCommandHandler.class);

    private final AuditRepository auditRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws AuditException {

        var
            audit =
            this.auditRepository.findById(input.auditId())
                                .orElseThrow(() -> new AuditException(AuditErrors.AUDIT_NOT_FOUND.defaultMessage(
                                        "The system cannot find the audit for ID [" + input.auditId().getId() +
                                                "].")));

        audit.outputInfo(input.outputInfo());

        this.auditRepository.saveAndFlush(audit);

        return new Output();

    }

}
