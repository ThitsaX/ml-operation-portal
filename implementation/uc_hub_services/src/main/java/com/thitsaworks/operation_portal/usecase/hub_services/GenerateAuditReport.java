package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateAuditReport {

    Output execute(Input input) throws ReportException;

    record Input(RealmId realmId,
                 Instant fromDate,
                 Instant toDate,
                 String timezoneoffset,
                 UserId participantUserId,
                 String action,
                 String fileType) { }

    record Output(byte[] rptBytes) { }

}
