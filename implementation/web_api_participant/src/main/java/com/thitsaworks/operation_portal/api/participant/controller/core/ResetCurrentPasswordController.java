package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.usecase.common.ResetCurrentPassword;
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
import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class ResetCurrentPasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentPasswordController.class);

    private final ResetCurrentPassword resetCurrentPassword;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/reset_password")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Reset password request : {}", this.objectMapper.writeValueAsString(request));

        ResetCurrentPassword.Output output = this.resetCurrentPassword.execute(
                new ResetCurrentPassword.Input(new Email(request.email()), request.password()));
        var response = new Response(output.updated());

        LOG.info("Reset password response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("email") String email,
            @NotNull @JsonProperty("new_password") String password
    ) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("updated") boolean updated
    ) implements Serializable {

    }

}
