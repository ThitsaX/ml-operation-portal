package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
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
    public ResponseEntity<Response> execute(@RequestParam(
                                                value = "participantId",
                                                required = false) String participantId,
                                            @RequestParam("fromDate") String fromDate,
                                            @RequestParam("toDate") String toDate,
                                            @RequestParam("timezoneOffset") String timezoneOffset,
                                            @RequestParam(
                                                value = "participantUserId",
                                                required = false) String participantUserId,
                                            @RequestParam(
                                                value = "action",
                                                required = false) String action,
                                            @RequestParam("fileType") String fileType)
        throws DomainException, JsonProcessingException {

        String showTimezone = timezoneOffset;

        if (!timezoneOffset.startsWith("-")) {
            showTimezone = timezoneOffset.replaceFirst(".", "+");
        }

        showTimezone = TimeZoneOffsetFormater.normalizeOffset(showTimezone);

        var output = this.generateAuditReport.execute(
            new GenerateAuditReport.Input(
                participantId == null || participantId.isBlank() ? null : new RealmId(Long.parseLong(participantId)),
                Instant.parse(fromDate),
                Instant.parse(toDate),
                showTimezone,
                participantUserId == null || participantUserId.isBlank() ? null :
                    new UserId(Long.parseLong(participantUserId)),
                action,
                fileType
            ));

        var response = new Response(output.rptBytes());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("generated") byte[] rptBytes) implements Serializable { }

}
