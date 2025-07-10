package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllAuduitController {
    private final GetAllAudit getAllAudit;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/get_all_audit")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        GetAllAudit.Output output = this.getAllAudit.execute(
            new GetAllAudit.Input(new RealmId(Long.parseLong(request.participantId)),
                                  request.participantUserId == null ? null :
                                      new UserId(Long.parseLong(request.participantUserId)),
                                  Instant.ofEpochSecond(request.fromDate),
                                  Instant.ofEpochSecond(request.toDate),
                                  request.actionName));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();

        for (var auditList : output.auditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(auditList.participantName(),
                                                     auditList.userName(),
                                                     auditList.actionName(),
                                                     auditList.inputInfo(),
                                                     auditList.outputInfo(),
                                                     auditList.actionDate().getEpochSecond()));
        }

        var response = new Response(auditInfoList);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull @JsonProperty("participant_id") String participantId,
        @JsonProperty("participant_user_id") String participantUserId,
        @NotNull @JsonProperty("from_date") Long fromDate,
        @NotNull @JsonProperty("to_date") Long toDate,
        @JsonProperty("action_name") String actionName
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("audit_info_list") List<AuditInfo> auditInfoList) {

        public record AuditInfo(
            @JsonProperty("participant_name") String participantName,
            @JsonProperty("user_name") String userName,
            @JsonProperty("action_name") String actionName,
            @JsonProperty("input_info") String inputInfo,
            @JsonProperty("output_info") String outputInfo,
            @JsonProperty("action_date") Long actionDate
        ) {}

    }

}

