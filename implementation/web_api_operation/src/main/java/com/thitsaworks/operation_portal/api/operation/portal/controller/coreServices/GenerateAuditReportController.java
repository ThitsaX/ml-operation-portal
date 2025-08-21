package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
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
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/secured/generateAuditReport")
    public ResponseEntity<Response> execute(@RequestParam(
                                                value = "participantId",
                                                required = false) String participantId,
                                            @RequestParam("fromDate") String fromDate,
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

        LOG.info("Generate Audit Report Request : participantId = [{}], fromDate = [{}], toDate = [{}], " +
                     "timezoneOffset = [{}], userId = [{}], actionId = [{}], fileType = [{}]",
                 participantId, fromDate, toDate, timezoneOffset, userId, actionId, fileType);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        String showTimezone = timezoneOffset;

        if (!timezoneOffset.startsWith("-")) {
            showTimezone = timezoneOffset.replaceFirst(".", "+");
        }

        showTimezone = TimeZoneOffsetFormater.normalizeOffset(showTimezone);

        var output = this.generateAuditReport.execute(new GenerateAuditReport.Input(
            participantId == null || participantId.isBlank() ? null : new RealmId(Long.parseLong(participantId)),
            Instant.parse(fromDate),
            Instant.parse(toDate),
            showTimezone,
            userId == null || userId.isBlank() ? null :
                new UserId(Long.parseLong(userId)),
            actionId == null || actionId.isBlank() ? null : new ActionId(Long.parseLong(actionId)),
            fileType,
            userContext.userId()));

        var response = new Response(output.rptBytes());

        LOG.info("Generate Audit Report Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] rptBytes) implements Serializable { }

}
