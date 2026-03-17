package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateRole;
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
public class CreateRoleController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateRoleController.class);

    private final CreateRole createRole;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/createRole")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Create Role Request : [{}]", this.objectMapper.writeValueAsString(request));

        var output = this.createRole.execute(new CreateRole.Input(request.name(), request.isDfsp()));

        var response = new Response(output.roleId().toString());

        LOG.info("Create Role Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("name") String name,
                          @JsonProperty("isDfsp") boolean isDfsp) implements Serializable { }

    public record Response(@JsonProperty("roleId") String roleId) implements Serializable { }

}
