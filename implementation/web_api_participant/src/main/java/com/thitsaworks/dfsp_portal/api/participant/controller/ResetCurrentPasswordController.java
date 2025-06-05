package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.api.participant.security.UserContext;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.usecase.common.ResetCurrentPassword;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
public class ResetCurrentPasswordController {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentPasswordController.class);

    @Autowired
    private ResetCurrentPassword resetCurrentPassword;

    @RequestMapping(value = "/secured/reset_password", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        ResetCurrentPassword.Output output = this.resetCurrentPassword.execute(
                new ResetCurrentPassword.Input(new Email(request.email), request.password));

        return new ResponseEntity<>(new Response(output.getUpdated()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("email")
        private String email;

        @NotNull
        @JsonProperty("new_password")
        private String password;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {


        @JsonProperty("updated")
        private boolean updated;

    }

}
