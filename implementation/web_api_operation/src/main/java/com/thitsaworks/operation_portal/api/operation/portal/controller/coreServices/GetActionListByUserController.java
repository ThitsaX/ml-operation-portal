package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetActionListByUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionListByUserController.class);

    private final GetActionListByUser getActionListByUser;

    @GetMapping(value = "/secured/getActionListByUser")
    public ResponseEntity<Response> execute() throws DomainException {

        var input = new GetActionListByUser.Input();
        var output = this.getActionListByUser.execute(input);

        List<Response.Action> actionNames = output.actionNames()
                      .stream()
                      .map(actionName -> new Response.Action(String.valueOf(actionName.actionId()
                                                                                      .getId()),
                              actionName.actionName()))
                      .collect(Collectors.toList());

        var response = new Response(actionNames);

        LOG.info("Get Action List By User Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(List<Action> actionList) {

        public record Action(String actionId, String actionName) implements Serializable {

        }

    }

}


