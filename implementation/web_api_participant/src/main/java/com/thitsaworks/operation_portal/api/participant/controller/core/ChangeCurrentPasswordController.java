package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.common.ChangeCurrentPassword;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class ChangeCurrentPasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeCurrentPasswordController.class);

    private final ChangeCurrentPassword changeCurrentPassword;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/change_password")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Change password request : {}", objectMapper.writeValueAsString(request));

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        ChangeCurrentPassword.Output output = this.changeCurrentPassword.execute(
                new ChangeCurrentPassword.Input(new PrincipalId(userContext.participantUserId().getId()),
                                                request.oldPassword(), request.newPassword()));

        var response = new Response(output.accessKey().getId().toString(),
                                    output.secretKey());

        LOG.info("Change password response : {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("old_password") String oldPassword,
            @NotNull @JsonProperty("new_password") String newPassword
    ) implements Serializable {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("access_key") String accessKey,
            @JsonProperty("secret_key") String secretKey
    ) implements Serializable {}

}