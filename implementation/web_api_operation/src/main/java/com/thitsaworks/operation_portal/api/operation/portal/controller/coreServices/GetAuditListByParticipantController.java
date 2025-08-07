package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditByParticipantList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAuditListByParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditListByParticipantController.class);

    private final GetAuditByParticipantList getAuditByParticipantList;

    @GetMapping("/secured/getAuditList")
    public ResponseEntity<Response> execute(
        @RequestParam("participantId") String participantId,
        @RequestParam("fromDate") Long fromDate,
        @RequestParam("toDate") Long toDate,
        @RequestParam("userId") String participantUserId,
        @RequestParam("actionName") String actionName) throws DomainException, JsonProcessingException {

        LOG.info(
            "Get Audit List Request: ParticipantId = [{}], FromDate = [{}], ToDate = [{}], UserId = [{}], ActionName = [{}]",
            participantId,
            fromDate,
            toDate,
            participantUserId,
            actionName);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GetAuditByParticipantList.Output output = this.getAuditByParticipantList.execute(
            new GetAuditByParticipantList.Input(new RealmId(Long.parseLong(participantId)),
                                                Instant.ofEpochSecond(fromDate),
                                                Instant.ofEpochSecond(toDate)
                ,
                                                (participantUserId != null && !participantUserId.isEmpty()) ?
                                                    new UserId(Long.parseLong(participantUserId)) : null,
                                                (actionName != null && !actionName.isEmpty()) ? actionName : null));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();
        for (var auditList : output.auditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(
                auditList.userName(),
                auditList.actionName(),
                auditList.actionDate()
                         .getEpochSecond()));
        }

        var response = new Response(auditInfoList);

        LOG.info("Get Audit List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("auditInfoList") List<AuditInfo> auditInfoList) implements Serializable {

        public record AuditInfo(
            @JsonProperty("userName") String userName,
            @JsonProperty("actionName") String actionName,
            @JsonProperty("actionDate") Long actionDate
        ) implements Serializable { }

    }

}
