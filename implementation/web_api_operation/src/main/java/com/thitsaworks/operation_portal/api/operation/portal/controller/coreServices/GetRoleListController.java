package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetRoleList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetRoleListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetRoleListController.class);

    private final GetRoleList getRoleList;

    @GetMapping("/secured/getRoleList")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getRoleList.execute(new GetRoleList.Input());

        var response = new Response(output.roleList()
                                          .stream()
                                          .map(role -> new Response.Role(role.roleId()
                                                                             .getId()
                                                                             .toString(),
                                                                         role.name(),
                                                                         role.active()))
                                          .toList());

        return ResponseEntity.ok(response);
    }

    public record Response(List<Role> roleList) {

        public record Role(String roleId,
                           String name,
                           boolean active) { }

    }

}
