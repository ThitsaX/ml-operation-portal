package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyUserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ModifyUserStatusController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyUserStatusController.class);

    private final ModifyUserStatus modifyUserStatus;

    @PostMapping("/secured/modifyUserStatus")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Modify User Status Request: [{}]", request);

        ModifyUserStatus.Output
            output =
            this.modifyUserStatus.execute(new ModifyUserStatus.Input(new UserId(Long.parseLong(request.userId())),
                                                                     request.userStatus()
                                                                .equalsIgnoreCase("ACTIVE") ?
                                                             PrincipalStatus.ACTIVE :
                                                             PrincipalStatus.INACTIVE));
        var response = new Response(output.removed());

        LOG.info("Modify User Status Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("userId") String userId,
                          @NotNull @NotBlank @JsonProperty("userStatus") String userStatus)
        implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isModified") boolean isModified) implements Serializable { }

}
