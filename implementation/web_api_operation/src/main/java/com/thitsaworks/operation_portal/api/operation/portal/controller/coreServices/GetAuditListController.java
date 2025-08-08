package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAuditListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditListController.class);

    private final GetAuditList getAuditList;

    @PostMapping("/secured/getAuditList")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Get Audit List Request: [{}]", request);

        GetAuditList.Output output = this.getAuditList.execute(
            new GetAuditList.Input(request.participantId() == null || request.participantId()
                                                                             .isBlank() ? null :
                                       new RealmId(Long.parseLong(request.participantId())),
                                   request.userId() == null ? null :
                                           new UserId(Long.parseLong(request.userId())),
                                   Instant.ofEpochSecond(request.fromDate()),
                                   Instant.ofEpochSecond(request.toDate()),
                                   request.actionName()));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();

        for (var auditList : output.auditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(auditList.participantName(),
                                                     auditList.userName(),
                                                     auditList.actionName(),
                                                     auditList.inputInfo(),
                                                     auditList.outputInfo(),
                                                     auditList.actionDate()
                                                              .getEpochSecond()));
        }

        var response = new Response(auditInfoList);

        LOG.info("Get Audit List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotNull @JsonProperty("participantId") String participantId,
                          @JsonProperty("userId") String userId,
                          @NotNull @JsonProperty("fromDate") Long fromDate,
                          @NotNull @JsonProperty("toDate") Long toDate,
                          @JsonProperty("actionName") String actionName
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("auditInfoList") List<AuditInfo> auditInfoList) implements Serializable {

        public record AuditInfo(@JsonProperty("participantName") String participantName,
                                @JsonProperty("userName") String userName,
                                @JsonProperty("actionName") String actionName,
                                @JsonProperty("inputInfo") String inputInfo,
                                @JsonProperty("outputInfo") String outputInfo,
                                @JsonProperty("actionDate") Long actionDate
        ) implements Serializable { }

    }

}

