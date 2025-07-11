package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateSettlementAuditReport extends
                                               UseCase<GenerateSettlementAuditReport.Input, GenerateSettlementAuditReport.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 String fileType,
                 String timezone) {}

    record Output(byte[] reportData) {}

}
