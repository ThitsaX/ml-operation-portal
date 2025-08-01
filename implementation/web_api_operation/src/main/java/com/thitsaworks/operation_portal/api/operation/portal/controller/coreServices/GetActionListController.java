package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetActionListController {

    private final GetActionList getActionList;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getActionList")
    public ResponseEntity<Response> execute() throws DomainException {
        var input = new GetActionList.Input();
        var output = this.getActionList.execute(input);

        List<Response.ActionName> actionNames = output.actionNames().stream()
                                                      .map(actionName -> new Response.ActionName(String.valueOf(actionName.actionId().getId()),
                                                                                                 actionName.actionName()))
                                                      .collect(Collectors.toList());

        return ResponseEntity.ok(new Response(actionNames));

    }

    public record Response(List<ActionName> actionNames) {
        public record ActionName(String actionId, String actionName) {}
    }
}


