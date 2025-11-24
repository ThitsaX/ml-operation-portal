package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateAction;
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
public class CreateActionController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateActionController.class);

    private final CreateAction createAction;

    @PostMapping("/secured/createAction")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException {

        LOG.info("Create Action Request : [{}]", request);

        var output = this.createAction.execute(new CreateAction.Input(new ActionCode(request.name()),
                                                                      request.description(),
                                                                      request.scope()));

        var response = new Response(output.actionId()
                                          .toString());

        LOG.info("Create Action Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("name") String name,
                          @NotNull @NotBlank @JsonProperty("description") String description,
                          @NotNull @NotBlank @JsonProperty("scope") String scope) implements Serializable { }

    public record Response(@JsonProperty("actionId") String actionId) implements Serializable {}

}
