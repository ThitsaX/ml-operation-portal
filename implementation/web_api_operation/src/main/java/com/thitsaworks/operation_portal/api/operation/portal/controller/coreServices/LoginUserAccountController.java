package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.util.MaskPassword;
import com.thitsaworks.operation_portal.usecase.operation_portal.LoginUserAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class LoginUserAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountController.class);

    private final LoginUserAccount loginUserAccount;

    @PostMapping("/public/loginUserAccount")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws Exception {

        LOG.info("Login User Account Request: [{}]", MaskPassword.toMaskedString(request));

        LoginUserAccount.Output output = this.loginUserAccount.execute(
            new LoginUserAccount.Input(new Email(request.email), request.password));

        var response = new Response(output.accessKey()
                                          .getId()
                                          .toString(), output.secretKey());

        LOG.info("Login User Account Response: [{}]", MaskPassword.toMaskedString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull
        @Pattern(
            regexp = Email.FORMAT,
            message = "Email must be with valid format.")
        @JsonProperty("email")
        String email,

        @NotBlank
        @JsonProperty("password")
        String password) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("accessKey") String accessKey,
                           @JsonProperty("secretKey") String secretKey) implements Serializable {

    }

}
