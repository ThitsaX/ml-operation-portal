package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAuditDetailById;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GetAuditDetailByIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuditDetailByIdController.class);

    private final GetAuditDetailById getAuditDetailById;

    @GetMapping("/secured/getAuditDetailById")
    public ResponseEntity<Response> execute(@RequestParam("auditId") String auditId) throws DomainException {

        LOG.info("Get Audit Detail By Id Request : auditId=[{}]", auditId);

        GetAuditDetailById.Output output = this.getAuditDetailById.execute(
            new GetAuditDetailById.Input(new AuditId(Long.parseLong(auditId))));

        var response = new Response(output.auditId()
                                          .getEntityId()
                                          .toString(),
                                    output.inputInfo(),
                                    output.outputInfo());

        LOG.info("Get Audit Detail By Id Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("auditId") String auditId,
                           @JsonProperty("inputInfo") String inputInfo,
                           @JsonProperty("outputInfo") String outputInfo
    ) implements Serializable { }

}
