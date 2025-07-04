package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransferState;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllTransferStateController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateController.class);

    private final GetAllTransferState getAllTransferState;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_all_transfer_state")
    public ResponseEntity<Response> execute() throws DomainException,
                                                     JsonProcessingException {

        LOG.info("Get all transfer state request : {}", "");

        GetAllTransferState.Output output = this.getAllTransferState.execute(new GetAllTransferState.Input());

        List<Response.TransferStateInfo> transferStateInfoList = new ArrayList<>();

        for (var idType : output.transferStateDataList()) {

            transferStateInfoList.add(new Response.TransferStateInfo(idType.getTransferStateId(),
                            idType.getTransferState()));
        }

        var response = new Response(transferStateInfoList);

        LOG.info("Get all transfer state response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("transfer_state_list") List<TransferStateInfo> transferStateInfoList) {

        public record TransferStateInfo(
                @JsonProperty("transfer_state_id") String transferStateId,
                @JsonProperty("transfer_state") String transferState
        ) {}

    }

}
