package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.usecase.common.LoginUserAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.NonNull;
import java.io.Serializable;

@RestController
public class LoginUserAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserAccountController.class);

    @Autowired
    private LoginUserAccount loginUserAccount;

    @RequestMapping(value = "/public/login_user_account", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        LoginUserAccount.Output output = this.loginUserAccount.execute(
                new LoginUserAccount.Input(new Email(request.email), request.password));

        return new ResponseEntity<>(new Response(output.getAccessKey().getId().toString(),
                                                 output.getSecretKey()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.")
        @JsonProperty("email")
        private String email;

        @NotBlank
        @JsonProperty("password")
        private String password;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Response implements Serializable {

        @JsonProperty("access_key")
        private String accessKey;

        @JsonProperty("secret_key")
        private String secretKey;

    }
}
