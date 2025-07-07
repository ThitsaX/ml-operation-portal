package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.common.GetAllAuditByParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllAuditController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditController.class);

    private final GetAllAuditByParticipant getAllAuditByParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getAllAudit")
    public ResponseEntity<Response> execute(
            @RequestParam("fromDate") Long fromDate,
            @RequestParam("toDate") Long toDate,
            @RequestParam("participantId") String participantId) throws DomainException, JsonProcessingException {

        LOG.info("Get all audit by participant request : fromDate = {}, toDate = {}, participantId = {}",
                 fromDate,
                 toDate,
                 participantId);

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetAllAuditByParticipant.Output output = this.getAllAuditByParticipant.execute(
                new GetAllAuditByParticipant.Input(new RealmId(Long.parseLong(participantId)),
                        Instant.ofEpochSecond(fromDate), Instant.ofEpochSecond(toDate)));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();
        for (var auditList : output.auditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(
                    auditList.userName(),
                    auditList.actionName(),
                    auditList.actionDate().getEpochSecond()));
        }

        var response = new Response(auditInfoList);

        LOG.info("Get all audit by participant response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("audit_info_list") List<AuditInfo> auditInfoList) {

        public record AuditInfo(
                @JsonProperty("userName") String userName,
                @JsonProperty("actionName") String actionName,
                @JsonProperty("actionDate") Long actionDate
        ) {}

    }

}
