package com.thitsaworks.operation_portal.core.report_download.model;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaInstantConverter;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_report_request")
@Getter
@NoArgsConstructor
public class ReportDownloadRequest extends JpaEntity<ReportDownloadRequestId> {

    @EmbeddedId
    private ReportDownloadRequestId requestId;

    @Column(name = "report_type")
    private String reportType;

    @Column(name = "params_signature")
    private String paramsSignature;

    @Column(name = "data_version")
    private LocalDate dataVersion;

    @Column(name = "status")
    private String status;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "finished_date")
    @Convert(converter = JpaInstantConverter.class)
    private Instant finishedDate;

    public ReportDownloadRequest(String reportType,
                                 String paramsSignature,
                                 LocalDate dataVersion,
                                 String status,
                                 String fileType,
                                 Instant createdDate,
                                 Instant updatedDate) {

        this.requestId = new ReportDownloadRequestId(Snowflake.get().nextId());
        this.reportType = reportType;
        this.paramsSignature = paramsSignature;
        this.dataVersion = dataVersion;
        this.status = status;
        this.fileType = fileType;
        this.setCreatedAt(createdDate);
        this.setUpdatedAt(updatedDate);
    }

    @Override
    public ReportDownloadRequestId getId() {

        return this.requestId;
    }

    public void status(String status) {

        this.status = status;
    }

    public void fileUrl(String fileUrl) {

        this.fileUrl = fileUrl;
    }

    public void errorMessage(String errorMessage) {

        this.errorMessage = errorMessage;
    }

    public void updatedDate(Instant updatedDate) {

        this.setUpdatedAt(updatedDate);
    }

    public void finishedDate(Instant finishedDate) {

        this.finishedDate = finishedDate;
    }
}
