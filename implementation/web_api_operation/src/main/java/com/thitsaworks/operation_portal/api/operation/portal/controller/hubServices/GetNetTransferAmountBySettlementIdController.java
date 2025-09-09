package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountBySettlementId;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountByWindowId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetNetTransferAmountBySettlementIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountBySettlementIdController.class);

    private final GetNetTransferAmountBySettlementId getNetTransferAmountBySettlementId;

    @GetMapping(value = "/secured/getNetTransferAmountBySettlementId")
    public ResponseEntity<GetNetTransferAmountBySettlementIdController.Response> execute(@RequestParam
                                                                                     @NotNull(message = "settlementId is required") int settlementId)

    throws DomainException, JsonProcessingException {

        LOG.info("Get Net Transfer Amount By Settlement Id Request : [{}]", settlementId);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();
        var
            output =
            this.getNetTransferAmountBySettlementId.execute(new GetNetTransferAmountBySettlementId.Input(
                settlementId));

        var response = new Response(output.settlementId(),
                                    output.settlementWindowIds(),
                                    output.windowOpenedDate(),
                                    output.windowClosedDate(),
                                    output.details()
                                          .stream()
                                          .map(detail -> new Detail(
                                              detail.participantName(),
                                              detail.debitAmount(),
                                              detail.creditAmount(),
                                              detail.currency()
                                          ))
                                          .toList()
        );

        LOG.info("Get Net Transfer Amount By Window Id Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(int settlementId,
                           String settlementWindowIds,
                           String windowOpenedDate,
                           String windowClosedDate,
                           List<Detail> details
    ) implements Serializable { }

    public record Detail(String participantName,
                         BigDecimal debitAmount,
                         BigDecimal creditAmount,
                         String currency) implements Serializable { }

}