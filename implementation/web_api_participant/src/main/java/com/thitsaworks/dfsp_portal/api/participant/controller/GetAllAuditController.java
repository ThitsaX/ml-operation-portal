package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.api.participant.security.UserContext;
import com.thitsaworks.dfsp_portal.audit.identity.UserId;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.iam.identity.RealmId;
import com.thitsaworks.dfsp_portal.usecase.common.GetAllAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAllAuditController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditController.class);

    @Autowired
    private GetAllAudit getAllAudit;

    @RequestMapping(value = "/secured/get_all_audit", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetAllAudit.Output output = this.getAllAudit.execute(
                new GetAllAudit.Input(new RealmId(Long.parseLong(request.participantId)),
                        request.participantUserId == null ? null :
                                new UserId(Long.parseLong(request.participantUserId)),
                        Instant.ofEpochSecond(request.fromDate), Instant.ofEpochSecond(request.toDate)));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();

        for (var auditList : output.getAuditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(auditList.getParticipantName(),
                    auditList.getUserName(),
                    auditList.getActionName(),
                    auditList.getInputInfo(),
                    auditList.getOutputInfo(),
                    auditList.getActionDate().getEpochSecond()));
        }

        return new ResponseEntity<>(new Response(auditInfoList), HttpStatus.OK);

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("participant_id")
        private String participantId;

        @JsonProperty("participant_user_id")
        private String participantUserId;

        @NotNull
        @JsonProperty("from_date")
        private Long fromDate;

        @NotNull
        @JsonProperty("to_date")
        private Long toDate;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("audit_info_list")
        private List<AuditInfo> auditInfoList;

        @Value
        public static class AuditInfo implements Serializable {

            @JsonProperty("participant_name")
            private String participantName;

            @JsonProperty("user_name")
            private String userName;

            @JsonProperty("action_name")
            private String actionName;

            @JsonProperty("input_info")
            private String inputInfo;

            @JsonProperty("output_info")
            private String outputInfo;

            @JsonProperty("action_date")
            private Long actionDate;

        }

    }

}
