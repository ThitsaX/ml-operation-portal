package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetMadeByList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetMadeByListController {

    private final GetMadeByList getMadeByList;

    @GetMapping(value = "/secured/getMadeByList")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException {

        GetMadeByList.Input input = new GetMadeByList.Input(new ParticipantId(Long.parseLong(participantId)));
        GetMadeByList.Output output = this.getMadeByList.execute(input);

        Set<Response.User>
            users =
            output.madeBy()
                  .stream()
                  .map(user -> new Response.User(user.userId()
                                                     .getId()
                                                     .toString(),
                                                 user.name()))
                  .collect(Collectors.toSet());

        return ResponseEntity.ok(new Response(users));
    }

    public record Response(Set<User> madeByList) {

        public record User(String userId,
                           String name) { }

    }

}