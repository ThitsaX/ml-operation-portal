package com.thitsaworks.operation_portal.api.operation.portal.controller.engineServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RefreshIAMEngine;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class RefreshIAMEngineController {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshIAMEngineController.class);

    private final RefreshIAMEngine refreshIAMEngine;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/refreshIAMEngine")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        var output = this.refreshIAMEngine.execute(new RefreshIAMEngine.Input());

        var response = new Response(output.refreshed());

        LOG.info("Refresh IAM Engine Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("refreshed") boolean refreshed) implements Serializable { }

}
