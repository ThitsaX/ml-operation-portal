package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.common.GetAllAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class GetAllActionController {

    private final GetAllAction getAllAction;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getActions")
    public ResponseEntity<Response> execute() throws DomainException {

        GetAllAction.Output output = this.getAllAction.execute(new GetAllAction.Input());

        Response response = new Response(output.actionNames());
        return ResponseEntity.ok(response);
    }

    public record Response(Set<String> actionNames) {
    }

}