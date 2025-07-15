package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.GetMadeBy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetMadeByController {

    private final GetMadeBy getMadeBy;

    @GetMapping(value = "/secured/getMydebyList")
    public ResponseEntity<Response> execute() throws DomainException {

        GetMadeBy.Input input = new GetMadeBy.Input();
        GetMadeBy.Output output = getMadeBy.execute(input);

        Set<Response.User> users = output.madeBy().stream()
                                         .map(user -> new Response.User(user.userId().getId()))
                                         .collect(Collectors.toSet());

        return ResponseEntity.ok(new Response(users));
    }
    public record Response(Set<User> MadeBy) {

        public record User(Long UserId) { }

    }}