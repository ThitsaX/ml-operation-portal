package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateTransactionDetailReport extends
                                               UseCase<GenerateTransactionDetailReport.Input, GenerateTransactionDetailReport.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 String state,
                 String dfspId,
                 String fileType,
                 String timezone) {}

    record Output(ReportDownloadRequestId requestId,
                  FileDownloadStatus status,
                  String fileUrl,
                  String fileKey,
                  String paramsSignature) {}

}
