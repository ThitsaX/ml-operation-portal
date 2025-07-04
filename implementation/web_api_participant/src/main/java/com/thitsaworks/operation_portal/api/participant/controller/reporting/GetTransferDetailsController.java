package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneConverter;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetTransferDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class GetTransferDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsController.class);

    private final GetTransferDetails getTransferDetails;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_transfer_detail")
    public ResponseEntity<Response> execute(@Valid @RequestParam String transferId, String timezone)
            throws DomainException, ParseException, JsonProcessingException {

        LOG.info("Get transfer detail filters : transferId = {}, timezone = {}", transferId, timezone);

        GetTransferDetails.Output output = this.getTransferDetails.execute(new GetTransferDetails.Input(transferId));

        String convertedDate;

        var info = output.businessData();

        String showTimezone;

        if (timezone.startsWith("-")) {

            showTimezone = timezone.replaceFirst(".", "+");

        } else {

            showTimezone = timezone.replaceFirst(".", "-");
        }

        convertedDate =
                (TimeZoneConverter.convertTimeZone(info.getSubmittedOnDate().replace(" ", "T") + "Z", showTimezone) +
                        timezone).replace("T", " ").replace("Z", " ");

        TransferInfo transferInfo =
                new TransferInfo(info.getTransferId(), info.getState(), info.getType(), info.getCurrency(),
                        info.getAmount(), info.getPayer(), info.getPayerDetails(), info.getPayerDfsp(), info.getPayee(),
                        info.getPayeeDetails(), info.getPayeeDfsp(), info.getSettlementBatch(), convertedDate);

        var response = new Response(transferInfo);

        LOG.info("Get transfer detail response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("transfer_details") TransferInfo transferInfo
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TransferInfo(
            @JsonProperty("transfer_id") String transferId,
            @JsonProperty("state") String state,
            @JsonProperty("type") String type,
            @JsonProperty("currency") String currency,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("payer") String payer,
            @JsonProperty("payer_details") String payerDetails,
            @JsonProperty("payer_dfsp") String payerDfsp,
            @JsonProperty("payee") String payee,
            @JsonProperty("payee_details") String payeeDetails,
            @JsonProperty("payee_dfsp") String payeeDfsp,
            @JsonProperty("settlement_batch") String settlementBatch,
            @JsonProperty("submitted_on_date") String submittedOnDate
    ) {

    }

}
