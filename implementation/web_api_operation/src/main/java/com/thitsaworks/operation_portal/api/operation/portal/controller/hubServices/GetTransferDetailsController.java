package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class GetTransferDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailsController.class);

    private final GetTransferDetail getTransferDetail;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getTransferDetail")
    public ResponseEntity<Response> execute(@Valid @RequestParam String transferId, String timezone)
        throws DomainException, ParseException, JsonProcessingException {

        LOG.info("Get Transfer Detail Request : transferId =[{}], timezone = [{}]", transferId, timezone);

        GetTransferDetail.Output output = this.getTransferDetail.execute(new GetTransferDetail.Input(transferId,
                                                                                                     timezone));

        TransferDetailData transferDetailData = output.transferDetailData();

        TransferInfo transferInfo =
            new TransferInfo(transferDetailData.transferId(),
                             transferDetailData.quoteId(),
                             transferDetailData.transferState(),
                             transferDetailData.transferType(),
                             transferDetailData.subScenario(),
                             transferDetailData.currency(),
                             transferDetailData.amountType(),
                             transferDetailData.quoteAmount(),
                             transferDetailData.transferAmount(),
                             transferDetailData.payeeReceivedAmount(),
                             transferDetailData.payeeDfspFee(),
                             transferDetailData.payeeDfspCommission(),
                             transferDetailData.submittedOnDate(),
                             transferDetailData.windowId(),
                             transferDetailData.settlementId());

        PartyInfo payerInfo = new PartyInfo(transferDetailData.payerInformation()
                                                              .idType(),
                                            transferDetailData.payerInformation()
                                                              .idValue(),
                                            transferDetailData.payerInformation()
                                                              .dfspId(),
                                            transferDetailData.payerInformation()
                                                              .name());

        PartyInfo payeeInfo = new PartyInfo(transferDetailData.payeeInformation()
                                                              .idType(),
                                            transferDetailData.payeeInformation()
                                                              .idValue(),
                                            transferDetailData.payeeInformation()
                                                              .dfspId(),
                                            transferDetailData.payeeInformation()
                                                              .name());

        TransferErrorInfo errorInfo = new TransferErrorInfo(transferDetailData.transferErrorInfo()
                                                                              .errorCode(),
                                                            transferDetailData.transferErrorInfo()
                                                                              .errorDescription());

        var response = new Response(transferInfo, payerInfo, payeeInfo, errorInfo);

        LOG.info("Get Transfer Detail Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("transferDetails") TransferInfo transferInfo,
        @JsonProperty("payerInformation") PartyInfo payerInfo,
        @JsonProperty("payeeInformation") PartyInfo payeeInfo,
        @JsonProperty("errorInformation") TransferErrorInfo errorInfo
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TransferInfo(@JsonProperty("transferId") String transferId,
                               @JsonProperty("quoteId") String quoteId,
                               @JsonProperty("transferState") String transferState,
                               @JsonProperty("transferType") String transferType,
                               @JsonProperty("subScenario") String subScenario,
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
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PartyInfo(@JsonProperty("idType") String idType,
                            @JsonProperty("idValue") String idValue,
                            @JsonProperty("dfspId") String dfspId,
                            @JsonProperty("name") String name) implements Serializable { }

    public record TransferErrorInfo(@JsonProperty("errorCode") String errorCode,
                                    @JsonProperty("errorDescription") String errorDescription)
        implements Serializable { }

}
