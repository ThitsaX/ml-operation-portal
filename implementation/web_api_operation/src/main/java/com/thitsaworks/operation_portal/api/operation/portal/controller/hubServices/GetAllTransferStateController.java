package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllTransferState;
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

    @GetMapping("/secured/getAllTransferState")
    public ResponseEntity<Response> execute() throws DomainException,
                                                     JsonProcessingException {

        GetAllTransferState.Output output = this.getAllTransferState.execute(new GetAllTransferState.Input());

        List<Response.TransferStateInfo> transferStateInfoList = new ArrayList<>();

        for (var idType : output.transferStateDataList()) {

            transferStateInfoList.add(new Response.TransferStateInfo(idType.getTransferStateId(),
                                                                     idType.getTransferState()));
        }

        var response = new Response(transferStateInfoList);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("transferStateInfoList") List<TransferStateInfo> transferStateInfoList) {

        public record TransferStateInfo(
            @JsonProperty("transferStateId") String transferStateId,
            @JsonProperty("transferState") String transferState
        ) { }

    }

}
