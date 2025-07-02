package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.hub_operator.CreateNewAnnouncement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.text.ParseException;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class CreateNewAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewAnnouncementController.class);

    private final CreateNewAnnouncement createNewAnnouncement;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/create_new_announcement")
    public ResponseEntity<Response> execute(
            @Valid @RequestBody CreateNewAnnouncementController.Request request)
            throws OperationPortalException, ParseException, JsonProcessingException {

        LOG.info("Create new announcement request: {}", objectMapper.writeValueAsString(request));

        CreateNewAnnouncement.Output output = this.createNewAnnouncement.execute(
                new CreateNewAnnouncement.Input(request.announcementTitle,
                                                request.announcementDetail,
                                                Instant.parse(request.announcementDate)));

        Response response = new Response(
                output.created());

        LOG.info("Create new announcement response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @JsonProperty("announcement_title")
            String announcementTitle,

            @NotNull
            @JsonProperty("announcement_detail")
            String announcementDetail,

            @NotNull
            @JsonProperty("announcement_date")
            String announcementDate
    ) implements Serializable {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("created")
            boolean created
    ) implements Serializable {}

}
