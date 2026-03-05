package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetReportDownloadStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GetReportDownloadStatusController {

    private static final Logger LOG = LoggerFactory.getLogger(GetReportDownloadStatusController.class);

    private final GetReportDownloadStatus getReportDownloadStatus;
    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getReportDownloadStatus")
    public ResponseEntity<Response> execute(@RequestParam("requestId") Long requestId)
            throws DomainException, JsonProcessingException {

        GetReportDownloadStatus.Output output =
                this.getReportDownloadStatus.execute(new GetReportDownloadStatus.Input(requestId));

        Response response = new Response(output.requestId(),
                                         output.reportType(),
                                         output.status(),
                                         output.fileUrl(),
                                         output.errorMessage(),
                                         output.finishedDate());

        LOG.info("Get Report Download Status Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, output.found() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("requestId") Long requestId,
                           @JsonProperty("reportType") String reportType,
                           @JsonProperty("status") String status,
                           @JsonProperty("fileUrl") String fileUrl,
                           @JsonProperty("errorMessage") String errorMessage,
                           @JsonProperty("finishedDate") String finishedDate) implements Serializable { }
}
