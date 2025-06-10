package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.usecase.common.ChangeCurrentPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

import jakarta.validation.Valid;
import lombok.NonNull;
import java.io.Serializable;

@RestController
public class ChangeCurrentPasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeCurrentPasswordController.class);

    @Autowired
    private ChangeCurrentPassword changeCurrentPassword;

    @RequestMapping(value = "/secured/change_password", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DFSPPortalException {

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        ChangeCurrentPassword.Output output = this.changeCurrentPassword.execute(
                new ChangeCurrentPassword.Input(new PrincipalId(userContext.getParticipantUserId().getId()),
                        request.oldPassword, request.newPassword));

        return new ResponseEntity<>(new Response(output.getAccessKey().getId().toString(),
                        output.getSecretKey()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @JsonProperty("old_password")
        private String oldPassword;

        @NonNull
        @JsonProperty("new_password")
        private String newPassword;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("access_key")
        private String accessKey;

        @JsonProperty("secret_key")
        private String secretKey;

    }

}
