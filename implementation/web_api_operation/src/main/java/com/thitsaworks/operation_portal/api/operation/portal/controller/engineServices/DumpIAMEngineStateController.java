package com.thitsaworks.operation_portal.api.operation.portal.controller.engineServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.data.EngineData;
import com.thitsaworks.operation_portal.usecase.operation_portal.DumpIAMEngineState;
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
public class DumpIAMEngineStateController {

    private static final Logger LOG = LoggerFactory.getLogger(DumpIAMEngineStateController.class);

    private final DumpIAMEngineState dumpIAMEngineState;

    @GetMapping("/secured/dumpIAMEngineState")
    public ResponseEntity<Response> execute() throws
                                              DomainException {

        var output = this.dumpIAMEngineState.execute(new DumpIAMEngineState.Input());

        var response = new Response(output.engineState());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("iamEngineState") EngineData engineState) implements Serializable { }

}
