package com.thitsaworks.operation_portal.api.operation.portal.controller.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_services.GenerateAuditReport;
import lombok.RequiredArgsConstructor;
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

    private final GenerateAuditReport generateAuditReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generate_audit_report")
    public ResponseEntity<Response> execute
        (@RequestParam("participant_id") String participantId,
         @RequestParam("from_date") String fromDate,
         @RequestParam("to_date") String toDate,
         @RequestParam("timezoneoffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        var output = this.generateAuditReport.execute(
            new GenerateAuditReport.Input(new RealmId(Long.parseLong(participantId)),
                                          Instant.parse(fromDate),
                                          Instant.parse(toDate),
                                          timezoneOffset));

        var response = new Response(output.rptBytes());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("generated") byte[] rptBytes) implements Serializable { }

}
