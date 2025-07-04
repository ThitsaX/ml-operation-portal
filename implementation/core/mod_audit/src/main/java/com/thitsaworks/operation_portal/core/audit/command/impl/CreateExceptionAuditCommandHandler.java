package com.thitsaworks.operation_portal.core.audit.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.exception.AuditNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateExceptionAuditCommandHandler implements CreateExceptionAuditCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateExceptionAuditCommandHandler.class);

    private final AuditRepository auditRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws AuditNotFoundException {

        var
            audit =
            this.auditRepository.findById(input.auditId())
                                .orElseThrow(() -> new AuditNotFoundException(input.auditId()
                                                                                   .getEntityId()
                                                                                   .toString()));

        audit.exception(input.exception());

        this.auditRepository.saveAndFlush(audit);

        return new Output();
    }

}
