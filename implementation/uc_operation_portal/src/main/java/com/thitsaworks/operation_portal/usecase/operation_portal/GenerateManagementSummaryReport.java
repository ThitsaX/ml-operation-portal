package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateManagementSummaryReport extends UseCase<GenerateManagementSummaryReport.Input, GenerateManagementSummaryReport.Output> {

    record Input(String startDate, String endDate, String timezoneOffset ,String fileType,Long userId) {}

    record Output(byte[] detailReportByte) {}
}
