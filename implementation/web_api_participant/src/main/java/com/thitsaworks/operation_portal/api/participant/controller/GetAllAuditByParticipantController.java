package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.iam.identity.RealmId;
import com.thitsaworks.operation_portal.usecase.common.GetAllAuditByParticipant;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAllAuditByParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAuditByParticipantController.class);

    @Autowired
    private GetAllAuditByParticipant getAllAuditByParticipant;

    @RequestMapping(value = "/secured/get_all_audit_by_participant", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(
            @RequestParam("from_date") Long fromDate,
            @RequestParam("to_date") Long toDate,
            @RequestParam("participantId") String participantId
            ) throws DFSPPortalException {

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetAllAuditByParticipant.Output output = this.getAllAuditByParticipant.execute(
                new GetAllAuditByParticipant.Input(new RealmId(Long.parseLong(participantId)),
                        Instant.ofEpochSecond(fromDate), Instant.ofEpochSecond(toDate)));

        List<Response.AuditInfo> auditInfoList = new ArrayList<>();
        for (var auditList : output.getAuditInfoList()) {
            auditInfoList.add(new Response.AuditInfo(
                    auditList.getUserName(),
                    auditList.getActionName(),
                    auditList.getActionDate().getEpochSecond()));
        }

        return new ResponseEntity<>(new Response(auditInfoList), HttpStatus.OK);

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


            @JsonProperty("user_name")
            private String userName;

            @JsonProperty("action_name")
            private String actionName;


            @JsonProperty("action_date")
            private Long actionDate;

        }

    }

}
