package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.GetMadeBy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class GetMadeByController {

    private final GetMadeBy getMadeBy;

    @GetMapping(value = "/secured/getMydebyList")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getMadeBy.execute(new GetMadeBy.Input());
        Response response = new Response(output.madeBy());
        return ResponseEntity.ok(response);

    }
    public record Response(Set<UserId> MadeBy) {
    }


}