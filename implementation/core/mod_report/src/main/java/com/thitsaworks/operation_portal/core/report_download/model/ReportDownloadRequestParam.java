package com.thitsaworks.operation_portal.core.report_download.model;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestParamId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_report_request_param")
@Getter
@NoArgsConstructor
public class ReportDownloadRequestParam extends JpaEntity<ReportDownloadRequestParamId> {

    @EmbeddedId
    private ReportDownloadRequestParamId requestParamId;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "request_id")
    )
    private ReportDownloadRequestId requestId;

    @Column(name = "param_key")
    private String paramKey;

    @Column(name = "param_value")
    private String paramValue;

    public ReportDownloadRequestParam(ReportDownloadRequestId requestId,
                                      String paramKey,
                                      String paramValue,
                                      java.time.Instant createdDate) {

        this.requestParamId = new ReportDownloadRequestParamId(Snowflake.get().nextId());
        this.requestId = requestId;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.setCreatedAt(createdDate);
        this.setUpdatedAt(createdDate);
    }

    @Override
    public ReportDownloadRequestParamId getId() {

        return this.requestParamId;
    }
}
