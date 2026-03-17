package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyRoleStatus;
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
public class ModifyRoleStatusController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyRoleStatusController.class);

    private final ModifyRoleStatus modifyRoleStatus;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modifyRoleStatus")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Modify Role Status Request : [{}]", this.objectMapper.writeValueAsString(request));

        var output = this.modifyRoleStatus.execute(
            new ModifyRoleStatus.Input(new RoleId(Long.parseLong(request.roleId())),
                                       request.roleStatus()));

        var response = new Response(output.modified());

        LOG.info("Modify Role Status Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("roleId") String roleId,
                          @JsonProperty("roleStatus") boolean roleStatus)
        implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isModified") boolean isModified) implements Serializable { }

}
