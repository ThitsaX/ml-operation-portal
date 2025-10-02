package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditByParticipantList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAuditListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditListController.class);

    private final GetAuditByParticipantList getAuditByParticipantList;

    @GetMapping("/secured/getAuditList")
    public ResponseEntity<Response> execute(
        @RequestParam("fromDate") String fromDate,
        @RequestParam("toDate") String toDate,
        @RequestParam("page") Integer page,
        @RequestParam("pageSize")Integer pageSize) throws DomainException, JsonProcessingException {

        LOG.info(
            "Get Audit List Request: fromDate = [{}], toDate = [{}]", fromDate, toDate);

        GetAuditByParticipantList.Output output = this.getAuditByParticipantList.execute(
            new GetAuditByParticipantList.Input(Instant.parse(fromDate),
                                                Instant.parse(toDate),
                                                page,
                                                pageSize));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();
        for (var auditInfo : output.auditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(
                auditInfo.date()
                         .getEpochSecond(),
                auditInfo.action(),
                auditInfo.madeBy() != null ? auditInfo.madeBy()
                                                      .getValue() : "unknown"));
        }

        var response = new Response(auditInfoList , output.total(), output.totalPages());

        LOG.info("Get Audit List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("auditInfoList") List<AuditInfo> auditInfoList,
                           @JsonProperty("total") long total,
                           @JsonProperty("totalPages")Integer totalPages) implements Serializable {

        public record AuditInfo(
            @JsonProperty("date") long date,
            @JsonProperty("action") String action,
            @JsonProperty("madeBy") String madeBy

        ) implements Serializable { }

    }

}
