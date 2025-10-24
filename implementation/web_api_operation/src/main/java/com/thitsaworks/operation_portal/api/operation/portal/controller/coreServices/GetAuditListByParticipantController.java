package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditListByParticipant;
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
public class GetAuditListByParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditListByParticipantController.class);

    private final GetAuditListByParticipant getAuditListByParticipant;

    @GetMapping("/secured/getAuditListByParticipant")
    public ResponseEntity<Response> execute(
        @RequestParam("fromDate") String fromDate,
        @RequestParam("toDate") String toDate,
        @RequestParam(
            value = "userId",
            required = false) String userId,
        @RequestParam(
            value = "actionId",
            required = false) String actionId,
        @RequestParam("page") Integer page,
        @RequestParam("pageSize") Integer pageSize) throws DomainException, JsonProcessingException {

        LOG.info("Get Audit List By Participant Request: fromDate = [{}], toDate = [{}], userId = [{}], actionId = [{}]",
                 fromDate,
                 toDate,
                 userId,
                 actionId);

        GetAuditListByParticipant.Output output = this.getAuditListByParticipant.execute(
            new GetAuditListByParticipant.Input(Instant.parse(fromDate),
                                                Instant.parse(toDate),
                                                userId == null || userId.isBlank() ? null :
                                                    new UserId(Long.parseLong(userId)),
                                                actionId == null || actionId.isBlank() ? null :
                                                    new ActionId(Long.parseLong(actionId)),
                                                page,
                                                pageSize));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();
        for (var auditInfo : output.auditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(auditInfo.auditId()
                                                              .getEntityId()
                                                              .toString(),
                                                     auditInfo.date()
                                                              .getEpochSecond(),
                                                     auditInfo.action(),
                                                     auditInfo.madeBy() != null ? auditInfo.madeBy()
                                                                                           .getValue() : "unknown"));
        }

        var response = new Response(auditInfoList, output.total(), output.totalPages());

        LOG.info("Get Audit List By Participant Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("auditInfoList") List<AuditInfo> auditInfoList,
                           @JsonProperty("total") long total,
                           @JsonProperty("totalPages") Integer totalPages) implements Serializable {

        public record AuditInfo(@JsonProperty("auditId") String auditId,
                                @JsonProperty("date") long date,
                                @JsonProperty("action") String action,
                                @JsonProperty("madeBy") String madeBy

        ) implements Serializable { }

    }

}
