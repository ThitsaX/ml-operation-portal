package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateAuditReport {

    Output execute(Input input) throws DomainException;

    record Input(RealmId realmId,
                 Instant fromDate,
                 Instant toDate,
                 String timezoneoffset,
                 UserId userId,
                 ActionId actionId,
                 String fileType,
                 UserId auditedById) { }

    record Output(byte[] rptBytes) { }

}
