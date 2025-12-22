package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CloseSettlementWindows;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class CloseSettlementWindowsController {

    private static final Logger LOG = LoggerFactory.getLogger(CloseSettlementWindowsController.class);

    private final CloseSettlementWindows closeSettlementWindows;

    @PostMapping(value = "/secured/closeSettlementWindow")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Close Settlement Window Request : [{}]", request);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();
        var
            output =
            this.closeSettlementWindows.execute(new CloseSettlementWindows.Input(request.state,
                                                                                 request.reason,
                                                                                 request.settlementWindowId));

        var response = new Response(output.settlementWindowId(), output.state(), output.reason(), output.createdDate(), output.closedDate(),
                                    output.changedDate());

        LOG.info("Close Settlement Window Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@JsonProperty("state") String state,
                          @JsonProperty("reason") String reason,
                          @JsonProperty("settlementWindowId") int settlementWindowId
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("settlementWindowId") int settlementWindowId,
                           @JsonProperty("state") String state,
                           @JsonProperty("reason") String reason,
                           @JsonProperty("createdDate") String createdDate,
                           @JsonProperty("closedDate") String closedDate,
                           @JsonProperty("changedDate") String changedDate
    ) implements Serializable { }

}