package com.thitsaworks.operation_portal.usecase.hub_services.impl;

import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import com.thitsaworks.operation_portal.usecase.hub_services.GenerateAuditReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateAuditReportHandler implements GenerateAuditReport {

    private final GenerateAuditReportCommand generateAuditReportCommand;

    @Override
    public Output execute(Input input) throws ReportException {

        var
            realmId =
            input.realmId() == null ? null : input.realmId()
                                                  .getEntityId()
                                                  .toString();

        var
            userId =
            input.participantUserId() == null ? null : input.participantUserId()
                                                            .getEntityId()
                                                            .toString();

        var output = this.generateAuditReportCommand.execute(new GenerateAuditReportCommand.Input(realmId,
                                                                                                  input.fromDate(),
                                                                                                  input.toDate(),
                                                                                                  input.timezoneoffset(),
                                                                                                  userId,
                                                                                                  input.action(),
                                                                                                  input.fileType()));

        return new Output(output.rptData());
    }

}
