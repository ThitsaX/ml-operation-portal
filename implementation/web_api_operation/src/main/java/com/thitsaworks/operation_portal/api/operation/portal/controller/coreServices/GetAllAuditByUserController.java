package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.common.GetAllAudit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllAuditController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditController.class);

    private final GetAllAudit getAllAudit;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/get_all_audit")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Get all audit request : {}", this.objectMapper.writeValueAsString(request));

        GetAllAudit.Output output = this.getAllAudit.execute(
                new GetAllAudit.Input(new RealmId(Long.parseLong(request.participantId)),
                        request.participantUserId == null ? null :
                                new UserId(Long.parseLong(request.participantUserId)),
                        Instant.ofEpochSecond(request.fromDate), Instant.ofEpochSecond(request.toDate)
                ,null));

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

        LOG.info("Get all audit response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("participant_id") String participantId,
            @JsonProperty("participant_user_id") String participantUserId,
            @NotNull @JsonProperty("from_date") Long fromDate,
            @NotNull @JsonProperty("to_date") Long toDate
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
