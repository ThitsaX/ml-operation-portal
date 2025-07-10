package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.usecase.core_services.LoginUserAccount;
import jakarta.validation.constraints.NotBlank;
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
import jakarta.validation.constraints.Pattern;

@RestController
@RequiredArgsConstructor
public class LoginUserAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountController.class);

    private final LoginUserAccount loginUserAccount;

    private final ObjectMapper objectMapper;

    @PostMapping("/public/login_user_account")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Login user account request : {}", this.objectMapper.writeValueAsString(request));

        LoginUserAccount.Output output = this.loginUserAccount.execute(
                new LoginUserAccount.Input(new Email(request.email), request.password));

        var response = new Response(output.accessKey().getId().toString(), output.secretKey());

        LOG.info("Login user account response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.")
            @JsonProperty("email")
            String email,

            @NotBlank
            @JsonProperty("password")
            String password
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("access_key")
            String accessKey,

            @JsonProperty("secret_key")
            String secretKey
    ) {

    }

}
