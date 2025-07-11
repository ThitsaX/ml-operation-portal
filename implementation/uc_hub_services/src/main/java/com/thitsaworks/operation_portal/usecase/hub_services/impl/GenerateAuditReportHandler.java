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

        var output = this.generateAuditReportCommand.execute(new GenerateAuditReportCommand.Input(input.realmId()
                                                                                                       .getId(),
                                                                                                  input.fromDate(),
                                                                                                  input.toDate(),
                                                                                                  input.timezoneoffset()));

        return new Output(output.rptData());
    }

}
