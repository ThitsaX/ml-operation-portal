package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateAuditReport extends UseCase<GenerateAuditReport.Input, GenerateAuditReport.Output> {

    record Input(Instant fromDate,
                 Instant toDate,
                 String timezoneOffset,
                 UserId userId,
                 ActionId actionId,
                 String fileType) { }

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  boolean reused,
                  String paramsSignature) { }

}
