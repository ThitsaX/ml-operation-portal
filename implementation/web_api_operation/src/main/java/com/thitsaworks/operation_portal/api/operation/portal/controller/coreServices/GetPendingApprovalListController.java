package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetPendingApprovalList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetPendingApprovalListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetPendingApprovalListController.class);

    private final GetPendingApprovalList getPendingApprovalList;

    @GetMapping(value = "/secured/getPendingApprovalList")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {


        var output = this.getPendingApprovalList.execute(new GetPendingApprovalList.Input());

        var response = new Response(output.pendingApprovalList()
                                          .stream()
                                          .map(request -> new Response.PendingApproval(request.approvalRequestId()
                                                                                              .getEntityId()
                                                                                              .toString(),
                                                                                       request.requestedAction(),
                                                                                       request.dfsp(),
                                                                                       request.currency(),
                                                                                       request.amount(),
                                                                                       request.requestedBy(),
                                                                                       request.requestedDateTime()
                                                                                              .getEpochSecond(),
                                                                                       request.action()
                                                                                              .name()))
                                          .toList());

        LOG.info("Get Pending Approval List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Request() implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<PendingApproval> pendingApprovalList) implements Serializable {

        public record PendingApproval(String approvalRequestId,
                                      String requestedAction,
                                      String dfsp,
                                      String currency,
                                      BigDecimal amount,
                                      String requestedBy,
                                      long requestedDateTime,
                                      String action) implements Serializable { }

    }

}
