package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetLiquidityProfileList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetLiquidityProfileListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetLiquidityProfileListController.class);

    private final GetLiquidityProfileList getLiquidityProfileList;

    @GetMapping(value = "/secured/getLiquidityProfileList")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException {

        LOG.info("Get Liquidity Profile List Request: ParticipantId = [{}]", participantId);

        var
            output =
            this.getLiquidityProfileList.execute(new GetLiquidityProfileList.Input(new ParticipantId(Long.parseLong(
                participantId))));

        var
            response = new Response(output.liquidityProfileInfoList()
                                          .stream()
                                          .map(profile -> new Response.LiquidityProfileInfo(profile.liquidityProfileId()
                                                                                                   .getEntityId()
                                                                                                   .toString(),
                                                                                            profile.bankName(),
                                                                                            profile.accountName(),
                                                                                            profile.accountNumber(),
                                                                                            profile.currency(),
                                                                                            profile.isActive()))
                                          .toList());

        LOG.info("Get Liquidity Profile List Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<LiquidityProfileInfo> liquidityProfileInfoList) implements Serializable {

        public record LiquidityProfileInfo(@JsonProperty("liquidityProfileId") String liquidityProfileId,
                                           @JsonProperty("bankName") String bankName,
                                           @JsonProperty("accountName") String accountName,
                                           @JsonProperty("accountNumber") String accountNumber,
                                           @JsonProperty("currency") String currency,
                                           @JsonProperty("isActive") Boolean isActive) implements Serializable {
        }

    }

}
