package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneConverter;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferList;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllTransferController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferController.class);

    private final GetTransferList getTransferList;

    @GetMapping("/secured/getAllTransfer")
    public ResponseEntity<Response> execute(
        @Valid @RequestParam String fromDate, String toDate, String transferId, String payerFspId,
        String payeeFspId,
        String payerIdentifierTypeId, String payeeIdentifierTypeId, String payerIdentifierValue,
        String payeeIdentifierValue,
        String currencyId,
        String transferStateId,
        String timezone) throws DomainException, ParseException, JsonProcessingException {

        LOG.info("Get All Transfer Request for fromDate = [{}], toDate = [{}], transferId = [{}], " +
                     "payerFspId = [{}], payeeFspId = [{}], payerIdentifierTypeId = [{}], " +
                     "payeeIdentifierTypeId = [{}], " + "payerIdentifierValue = [{}], " +
                     "payeeIdentifierValue = [{}], currencyId = [{}], transferStateId = [{}], timezone = [{}]",
                 fromDate, toDate, transferId, payerFspId, payeeFspId, payerIdentifierTypeId, payeeIdentifierTypeId,
                 payerIdentifierValue, payeeIdentifierValue, currencyId, transferStateId, timezone);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        String localFromDate = TimeZoneConverter.convertTimeZone(fromDate, timezone);

        String localToDate = TimeZoneConverter.convertTimeZone(toDate, timezone);

        String showTimezone;
        if (timezone.startsWith("-")) {
            showTimezone = timezone.replaceFirst(".", "+");
        } else {
            showTimezone = timezone.replaceFirst(".", "-");
        }

        GetTransferList.Output output = this.getTransferList.execute(
            new GetTransferList.Input(
                localFromDate,
                localToDate,
                transferId,
                payerFspId,
                payeeFspId,
                payerIdentifierTypeId,
                payeeIdentifierTypeId,
                payerIdentifierValue,
                payeeIdentifierValue,
                currencyId,
                transferStateId,
                new ParticipantUserId(userContext.userId()
                                                 .getId()),
                timezone));

        List<Response.TransferInfo> transferInfoList = new ArrayList<>();

        for (var transferInfo : output.transferInfoList()) {

            transferInfoList.add(new Response.TransferInfo(
                transferInfo.getTransferId(),
                transferInfo.getState(),
                transferInfo.getType(),
                transferInfo.getCurrency(),
                transferInfo.getAmount(),
                transferInfo.getPayerDfsp(),
                transferInfo.getPayeeDfsp(),
                transferInfo.getWindowId(),
                transferInfo.getSettlementBatch(),
                (TimeZoneConverter.convertTimeZone(transferInfo.getSubmittedOnDate()
                                                               .replace(" ", "T") + "Z",
                                                   showTimezone) + timezone).replace("T", " ")
                                                                            .replace("Z", " ")));

        }

        var response = new Response(transferInfoList);

        LOG.info("Get All Transfer Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public record Response(@JsonProperty("transferInfoList") List<TransferInfo> transferInfoList) {

        public record TransferInfo(
            @JsonProperty("transferId") String transferId,
            @JsonProperty("state") String state,
            @JsonProperty("type") String type,
            @JsonProperty("currency") String currency,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("payerDfsp") String payerDfsp,
            @JsonProperty("payeeDfsp") String payeeDfsp,
            @JsonProperty("window_Id") String windowId,
            @JsonProperty("settlementBatch") String settlementBatch,
            @JsonProperty("submittedOnDate") String submittedOnDate
        ) { }

    }

}
