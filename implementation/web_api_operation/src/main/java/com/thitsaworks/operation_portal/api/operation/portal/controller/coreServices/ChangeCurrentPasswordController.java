package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ChangeCurrentPassword;
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

    @PostMapping(value = "/secured/changePassword")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        ChangeCurrentPassword.Output output = this.changeCurrentPassword.execute(
            new ChangeCurrentPassword.Input(new PrincipalId(userContext.userId()
                                                                       .getId()),
                                            request.oldPassword(),
                                            request.newPassword()));

        var response = new Response(output.accessKey()
                                          .getId()
                                          .toString(),
                                    output.secretKey());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("oldPassword") String oldPassword,
                          @NotNull @JsonProperty("newPassword") String newPassword
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("accessKey") String accessKey,
                           @JsonProperty("secretKey") String secretKey
    ) implements Serializable { }

}