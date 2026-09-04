/*
 * Copyright (c) 2024-2026 ThitsaWorks Pte. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateFeeAmountSwiftReport;
import com.thitsaworks.operation_portal.usecase.operation_portal.GeneratePacsFeeAmountSwiftReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GeneratePacsFeeAmountSwiftReportController {

    private static final Logger LOG = LoggerFactory.getLogger(
        GeneratePacsFeeAmountSwiftReportController.class);

    private final GeneratePacsFeeAmountSwiftReport generatePacsFeeAmountSwiftReport;

    @PostMapping("/secured/generatePacsFeeAmountReport")
    public ResponseEntity<Response> execute(@RequestParam("settlementId") String settlementId,
                                            @RequestParam("currencyId") String currencyId,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Generate Pacs 029 Fee Amount Report : settlementId = [{}], currencyId = [{}], timezoneOffset = [{}]",
            settlementId, currencyId, timezoneOffset);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        UserContext userContext = (UserContext) SecurityContextHolder
                                                    .getContext()
                                                    .getAuthentication()
                                                    .getDetails();

        GeneratePacsFeeAmountSwiftReport.Output output = this.generatePacsFeeAmountSwiftReport.execute(
            new GeneratePacsFeeAmountSwiftReport.Input(
                settlementId, currencyId, timezone,
                userContext.userId().getId()));

        var response = new Response(output.reportData());

        LOG.info("Generate Pacs 029 Fee Amount Report Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] transactionAmountByte)
        implements Serializable { }

}
