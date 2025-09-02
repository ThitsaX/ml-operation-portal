package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByUserId;
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
public class GetActionListByUserIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetActionListByUserIdController.class);

    private final GetActionListByUserId getActionListByUserId;

    @GetMapping(value = "/secured/getActionListByUserId")
    public ResponseEntity<Response> execute() throws DomainException {

        LOG.info("Get Action List By User Id Request : [{}]", "");

        var input = new GetActionListByUserId.Input();
        var output = this.getActionListByUserId.execute(input);

        List<Response.Action>
            actionNames =
            output.actionNames()
                  .stream()
                  .map(actionName -> new Response.Action(String.valueOf(actionName.actionId()
                                                                                      .getId()),
                                                             actionName.actionName()))
                  .collect(Collectors.toList());

        var response = new Response(actionNames);

        LOG.info("Get Action List By User Id Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(List<Action> actionList) {

        public record Action(String actionId, String actionName) implements Serializable { }

    }

}


