package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetRoleListByParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetRoleListByParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetRoleListByParticipantController.class);

    private final GetRoleListByParticipant getRoleListByParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getRoleListByParticipant")
    public ResponseEntity<Response> execute(@RequestParam("participantName") String participantName)
        throws DomainException, JsonProcessingException {

        LOG.info("Get Role List By User Id Request : participantName : [{}]", participantName);

        var output = this.getRoleListByParticipant.execute(new GetRoleListByParticipant.Input(participantName));

        var response = new Response(output.roleList()
                                          .stream()
                                          .map(role -> new Response.Role(role.roleId()
                                                                             .getId()
                                                                             .toString(),
                                                                         role.name(),
                                                                         role.active()))
                                          .toList());

        LOG.info("Get Role List By User Id Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("roleList") List<Role> roleList) {

        public record Role(@JsonProperty("roleId") String roleId,
                           @JsonProperty("name") String name,
                           @JsonProperty("active") boolean active) implements Serializable { }

    }

}
