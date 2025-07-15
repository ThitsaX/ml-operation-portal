package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.core.audit.data.ActionData;
import com.thitsaworks.operation_portal.core.audit.data.AuditData;

import java.util.List;
import java.util.Optional;

public interface AuditQuery {
    List<AuditData> getAudits();

    Optional<AuditData> get(String Name) ;
}


