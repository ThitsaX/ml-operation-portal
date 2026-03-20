package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementIdWithParentParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementIdWithParentParticipantController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdWithParentParticipantController.class);

    private final GetSettlementIdWithParentParticipant getSettlementIdWithParentParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getSettlementIdWithParentParticipant")
    public ResponseEntity<Response> execute(@RequestParam("startDate") String startDate,
                                            @RequestParam("endDate") String endDate,
                                            @RequestParam(
                                                    value = "dfspId",
                                                    required = false) String dfspId,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
            throws DomainException, JsonProcessingException {

        LOG.info(
                "Get SettlementIdWithParentParticipant Request : startDate = [{}], endDate = [{}], dfspId = [{}], timezoneOffset = [{}]",
                startDate, endDate, dfspId, timezoneOffset);

        GetSettlementIdWithParentParticipant.Output output = this.getSettlementIdWithParentParticipant.execute(
                new GetSettlementIdWithParentParticipant.Input(Instant.parse(startDate),
                                                               Instant.parse(endDate),
                                                               (dfspId == null || dfspId.isBlank())
                                                                       ? null
                                                                       : Integer.parseInt(dfspId),
                                                               timezoneOffset));

        List<SettlementIdInfo> settlementIdInfoList = output.settlementIds()
                                                             .stream()
                                                             .map(idType -> new SettlementIdInfo(idType.getSettlementId()))
                                                             .sorted(
                                                                     Comparator.comparingInt(
                                                                                     d -> Integer.parseInt(((SettlementIdInfo) d).settlementId()))
                                                                               .reversed())
                                                             .toList();

        var response = new Response(settlementIdInfoList);

        LOG.info("Get SettlementIdWithParentParticipant Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("settlementIdDataList") List<SettlementIdInfo> settlementIdDataList
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SettlementIdInfo(
            @JsonProperty("settlementId") String settlementId
    ) {

    }
}
