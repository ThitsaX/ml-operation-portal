package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetMadeByList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetMadeByListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetMadeByListController.class);

    private final GetMadeByList getMadeByList;

    @GetMapping(value = "/secured/getMadeByList")
    public ResponseEntity<Response> execute()
        throws DomainException {

        LOG.info("Get Made By List Request : [{}]", "");

        GetMadeByList.Input input = new GetMadeByList.Input();
        GetMadeByList.Output output = this.getMadeByList.execute(input);

        Set<Response.User>
            users = output.madeBy()
                          .stream()
                          .map(user -> {
                              var email = user.email();
                              return new Response.User(user.userId()
                                                           .getId()
                                                           .toString(),
                                                       email == null ? "unknown" : email.getValue());
                          })
                          .collect(Collectors.toSet());

        LOG.info("Get Made By List Response: [{}]", users);

        return ResponseEntity.ok(new Response(users));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(Set<User> madeByList) {

        public record User(String userId,
                           String email) implements Serializable { }

    }

}