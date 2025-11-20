package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantUserListByParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetParticipantUserListByParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantUserListByParticipantController.class);

    private final GetParticipantUserListByParticipant getParticipantUserListByParticipant;

    @GetMapping(value = "/secured/getParticipantUserListByParticipant")
    public ResponseEntity<Response> execute() throws DomainException {

        GetParticipantUserListByParticipant.Input input = new GetParticipantUserListByParticipant.Input();
        GetParticipantUserListByParticipant.Output output = this.getParticipantUserListByParticipant.execute(input);

        Set<Response.User> users = output.madeBy()
                                         .stream()
                                         .map(user -> {
                                             var email = user.email();
                                             return new Response.User(
                                                 user.userId()
                                                     .getId()
                                                     .toString(),
                                                 email == null ? "unknown" : email.getValue()
                                             );
                                         })
                                         .sorted(Comparator.comparing(Response.User::email))
                                         .collect(Collectors.toCollection(LinkedHashSet::new));

        LOG.info("Get Participant User List By Participant Response : [{}]", users);

        return ResponseEntity.ok(new Response(users));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(Set<User> userList) {

        public record User(String userId,
                           String email) implements Serializable { }

    }

}