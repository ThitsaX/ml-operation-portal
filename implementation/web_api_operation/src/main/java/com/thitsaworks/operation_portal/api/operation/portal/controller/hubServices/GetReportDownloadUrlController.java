package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetReportDownloadUrl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GetReportDownloadUrlController {

    private static final Logger LOG = LoggerFactory.getLogger(GetReportDownloadUrlController.class);

    private final GetReportDownloadUrl getReportDownloadUrl;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/getReportDownloadUrl")
    public ResponseEntity<Response> execute(@RequestBody Request request)
        throws DomainException, JsonProcessingException {

        GetReportDownloadUrl.Output output = this.getReportDownloadUrl.execute(
            new GetReportDownloadUrl.Input(
                new ReportDownloadRequestId(Long.parseLong(request.requestId()))));

        Response response = new Response(
            output.status() == null ? null : output.status().name(),
            output.fileUrl(),
            output.fileKey());

        LOG.info("Get Report Download Url Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, output.found() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@JsonProperty("requestId") String requestId) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("status") String status,
                           @JsonProperty("fileUrl") String fileUrl,
                           @JsonProperty("fileKey") String fileKey) implements Serializable { }
}
