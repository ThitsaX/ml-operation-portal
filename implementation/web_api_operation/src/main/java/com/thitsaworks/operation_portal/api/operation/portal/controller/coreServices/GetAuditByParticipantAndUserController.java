package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditByParticipantAndUser;
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
public class GetAuditByParticipantAndUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditByParticipantAndUserController.class);

    private final GetAuditByParticipantAndUser getAuditByParticipantAndUser;

    @GetMapping("/secured/getAuditByParticipantAndUser")
    public ResponseEntity<Response> execute(@RequestParam ("participantId") String participantId,
                                            @RequestParam ("userId") String userId,
                                            @RequestParam("fromDate") String fromDate,
                                            @RequestParam("toDate") String toDate,
                                            @RequestParam("actionName") String actionName,
                                            @RequestParam("page") Integer page,
                                            @RequestParam("pageSize") Integer pageSize
    )
        throws DomainException, JsonProcessingException {



        GetAuditByParticipantAndUser.Output output = this.getAuditByParticipantAndUser.execute(
            new GetAuditByParticipantAndUser.Input(new RealmId(Long.parseLong(participantId)),
                    new UserId(Long.parseLong(userId)),
                    Instant.parse(fromDate),
                    Instant.parse(toDate),
                    String.valueOf(actionName),
                    page,
                    pageSize));

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

        var response = new Response(auditInfoList ,output.total(),output.totalPages());

        LOG.info("Get Audit List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("auditInfoList") List<AuditInfo> auditInfoList,
                           @JsonProperty("total") long total,
                           @JsonProperty("totalPages") Integer totalPages) implements Serializable {

        public record AuditInfo(@JsonProperty("participantName") String participantName,
                                @JsonProperty("userName") String userName,
                                @JsonProperty("actionName") String actionName,
                                @JsonProperty("inputInfo") String inputInfo,
                                @JsonProperty("outputInfo") String outputInfo,
                                @JsonProperty("actionDate") Long actionDate
        ) implements Serializable { }

    }

}

