package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetRoleListByParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetRoleListByParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetRoleListByParticipantController.class);

    private final GetRoleListByParticipant getRoleListByParticipant;

    @GetMapping("/secured/getRoleListByParticipant")
    public ResponseEntity<Response> execute() throws DomainException {

        LOG.info("Get Role List By User Id Request : [{}]", "");

        var output = this.getRoleListByParticipant.execute(new GetRoleListByParticipant.Input());

        var response = new Response(output.roleList()
                                          .stream()
                                          .map(role -> new Response.Role(role.roleId()
                                                                             .getId()
                                                                             .toString(),
                                                                         role.name(),
                                                                         role.active()))
                                          .toList());

        LOG.info("Get Role List By User Id Response: [{}]", response);

        return ResponseEntity.ok(response);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<Role> roleList) {

        public record Role(String roleId,
                           String name,
                           boolean active) implements Serializable { }

    }

}
