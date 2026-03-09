package com.thitsaworks.operation_portal.core.reporting.download.generator;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.io.IOException;
import java.util.Map;

public interface ReportTypeGenerator {

    ReportType reportType();

    ReportGeneratedFile generate(ReportDownloadRequest request,
                                 Map<String, String> params) throws ReportException, IOException;
}

