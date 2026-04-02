package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateAuditReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class GenerateAuditReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateAuditReportController.class);

    private final GenerateAuditReport generateAuditReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generateAuditReport")
    public ResponseEntity<Response> execute(@RequestParam("fromDate") String fromDate,
                                            @RequestParam("toDate") String toDate,
                                            @RequestParam("timezoneOffset") String timezoneOffset,
                                            @RequestParam(
                                                value = "userId",
                                                required = false) String userId,
                                            @RequestParam(
                                                value = "actionId",
                                                required = false) String actionId,
                                            @RequestParam("fileType") String fileType)
        throws DomainException, JsonProcessingException {

        LOG.info("Generate Audit Report Request : fromDate = [{}], toDate = [{}], " +
                     "timezoneOffset = [{}], userId = [{}], actionId = [{}], fileType = [{}]",
                 fromDate, toDate, timezoneOffset, userId, actionId, fileType);

        var showTimezone = TimeZoneOffsetFormater.normalizeOffset(timezoneOffset);

        var output = this.generateAuditReport.execute(new GenerateAuditReport.Input(
            Instant.parse(fromDate),
            Instant.parse(toDate),
            showTimezone,
            userId == null || userId.isBlank() ? null :
                new UserId(Long.parseLong(userId)),
            actionId == null || actionId.isBlank() ? null : new ActionId(Long.parseLong(actionId)),
            fileType));

        var response = new Response(
            output.requestId().getEntityId().toString(), output.status().name(), output.fileUrl(), output.paramsSignature());

        LOG.info("Generate Audit Report Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("requestId") String requestId,
                           @JsonProperty("status") String status,
                           @JsonProperty("fileUrl") String fileUrl,
                           @JsonProperty("paramsSignature") String paramsSignature)
        implements Serializable { }

}
