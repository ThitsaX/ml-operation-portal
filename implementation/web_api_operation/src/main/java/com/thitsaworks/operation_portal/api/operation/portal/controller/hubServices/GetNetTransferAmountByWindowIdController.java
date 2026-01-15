package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountByWindowId;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetNetTransferAmountByWindowIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountByWindowIdController.class);

    private final GetNetTransferAmountByWindowId getNetTransferAmountByWindowId;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getNetTransferAmountByWindowId")
    public ResponseEntity<Response> execute(@RequestParam
                                            @NotNull(message = "settlementWindowId is required") int settlementWindowId)
        throws DomainException, JsonProcessingException {

        LOG.info("Get Net Transfer Amount By Window Id Request : [{}]", settlementWindowId);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();
        var
            output =
            this.getNetTransferAmountByWindowId.execute(new GetNetTransferAmountByWindowId.Input(
                settlementWindowId));

        var response = new Response(output.settlementWindowId(),
                                    output.windowOpenedDate(),
                                    output.windowClosedDate(),
                                    output.details()
                                          .stream()
                                          .map(detail -> new Detail(
                                              detail.participantName(),
                                              detail.debitAmount()
                                                    .abs(),
                                              detail.creditAmount()
                                                    .abs(),
                                              detail.currency()
                                          ))
                                          .toList()
        );

        LOG.info("Get Net Transfer Amount By Window Id Response : [{}]",
                 this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("settlementWindowId") int settlementWindowId,
                           @JsonProperty("windowOpenedDate") String windowOpenedDate,
                           @JsonProperty("windowClosedDate") String windowClosedDate,
                           @JsonProperty("details") List<Detail> details
    ) implements Serializable { }

    public record Detail(@JsonProperty("participantName") String participantName,
                         @JsonProperty("debitAmount") BigDecimal debitAmount,
                         @JsonProperty("creditAmount") BigDecimal creditAmount,
                         @JsonProperty("currency") String currency) implements Serializable { }

}