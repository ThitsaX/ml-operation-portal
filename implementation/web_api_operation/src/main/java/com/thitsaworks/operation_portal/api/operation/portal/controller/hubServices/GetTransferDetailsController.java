package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneConverter;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class GetTransferDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsController.class);

    private final GetTransferDetails getTransferDetails;

    @GetMapping("/secured/getTransferDetail")
    public ResponseEntity<Response> execute(@Valid @RequestParam String transferId, String timezone)
        throws DomainException, ParseException, JsonProcessingException {

        GetTransferDetails.Output output = this.getTransferDetails.execute(new GetTransferDetails.Input(transferId));

        TransferDetailData transferDetailData = output.transferDetailData();

        String showTimezone;

        String convertedDate;

        if (timezone.startsWith("-")) {
            showTimezone = timezone.replaceFirst(".", "+");
        } else {
            showTimezone = timezone.replaceFirst(".", "-");
        }

        convertedDate = (TimeZoneConverter.convertTimeZone(transferDetailData.submittedOnDate().replace(" ", "T") + "Z",
                                                           showTimezone) + timezone).replace("T", " ").replace("Z",
                                                                                                               " ");

        TransferInfo transferInfo =
                new TransferInfo(transferDetailData.transferId(),
                                 transferDetailData.quoteId(),
                                 transferDetailData.transferState(),
                                 transferDetailData.transferType(),
                                 transferDetailData.currency(),
                                 transferDetailData.amountType(),
                                 transferDetailData.quoteAmount(),
                                 transferDetailData.transferAmount(),
                                 transferDetailData.payeeReceivedAmount(),
                                 transferDetailData.payeeDfspFee(),
                                 transferDetailData.payeeDfspCommission(),
                                 convertedDate,
                                 transferDetailData.windowId(),
                                 transferDetailData.settlementId());

        PartyInfo payerInfo = new PartyInfo(transferDetailData.payerInformation().idType(),
                                            transferDetailData.payerInformation().idValue(),
                                            transferDetailData.payerInformation().dfspId(),
                                            transferDetailData.payerInformation().name());

        PartyInfo payeeInfo = new PartyInfo(transferDetailData.payeeInformation().idType(),
                                            transferDetailData.payeeInformation().idValue(),
                                            transferDetailData.payeeInformation().dfspId(),
                                            transferDetailData.payeeInformation().name());

        TransferErrorInfo errorInfo = new TransferErrorInfo(transferDetailData.transferErrorInfo().errorCode(),
                                                            transferDetailData.transferErrorInfo().errorDescription());

        return new ResponseEntity<>(new Response(transferInfo, payerInfo, payeeInfo, errorInfo), HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("transferDetails") TransferInfo transferInfo,
            @JsonProperty("payerInformation") PartyInfo payerInfo,
            @JsonProperty("payeeInformation") PartyInfo payeeInfo,
            @JsonProperty("errorInformation") TransferErrorInfo errorInfo
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TransferInfo(
        @JsonProperty("transferId") String transferId,
        @JsonProperty("quoteId") String quoteId,
        @JsonProperty("transferState") String transferState,
        @JsonProperty("transferType") String transferType,
        @JsonProperty("currency") String currency,
        @JsonProperty("amountType") String amountType,
        @JsonProperty("quoteAmount") String quoteAmount,
        @JsonProperty("transferAmount") String transferAmount,
        @JsonProperty("payeeReceivedAmount") String payeeReceivedAmount,
        @JsonProperty("payeeDfspFeeAmount") String payeeDfspFeeAmount,
        @JsonProperty("payeeDfspCommissionAmount") String payeeDfspCommissionAmount,
        @JsonProperty("submittedOnDate") String submittedOnDate,
        @JsonProperty("windowId") String windowId,
        @JsonProperty("settlementId") String settlementId
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PartyInfo(

            @JsonProperty("idType") String idType,

            @JsonProperty("idValue") String idValue,

            @JsonProperty("dfspId") String dfspId,

            @JsonProperty("name") String name) {}

    public record TransferErrorInfo(

            @JsonProperty("errorCode") String errorCode,

            @JsonProperty("errorDescription") String errorDescription) {}

}
