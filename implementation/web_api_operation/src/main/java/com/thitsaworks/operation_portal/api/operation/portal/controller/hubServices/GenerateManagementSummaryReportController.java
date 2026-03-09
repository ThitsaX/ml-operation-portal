package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateManagementSummaryReport;
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

@RestController
@RequiredArgsConstructor
public class GenerateManagementSummaryReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateManagementSummaryReportController.class);

    private final GenerateManagementSummaryReport generateManagementSummaryReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generateManagementSummaryReport")
    public ResponseEntity<Response> execute(@RequestParam("startDate") String startDate,
                                            @RequestParam("endDate") String endDate,
                                            @RequestParam("timezoneOffset") String timezoneOffset,
                                            @RequestParam("fileType") String fileType)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Generate Management Summary Report Request : startDate = [{}], endDate = [{}], timezoneOffset = [{}], fileType = [{}]",
            startDate,
            endDate,
            timezoneOffset,
            fileType);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GenerateManagementSummaryReport.Output
            output =
            this.generateManagementSummaryReport.execute(new GenerateManagementSummaryReport.Input(
                startDate,
                endDate,
                timezone,
                fileType,
                userContext.userId()
                           .getId()));

        var response = new Response(
            output.requestId().getEntityId().toString(),
            output.status().name(),
            output.fileUrl(),
            output.reused(),
            output.paramsSignature());

        LOG.info("Generate Management Summary Report Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("requestId") String requestId,
                           @JsonProperty("status") String status,
                           @JsonProperty("fileUrl") String fileUrl,
                           @JsonProperty("reused") boolean reused,
                           @JsonProperty("paramsSignature") String paramsSignature)
        implements Serializable { }

}
