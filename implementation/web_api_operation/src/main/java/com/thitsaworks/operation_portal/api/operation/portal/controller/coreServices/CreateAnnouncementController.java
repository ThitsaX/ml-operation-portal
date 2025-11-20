package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateAnnouncement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class CreateAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnnouncementController.class);

    private final CreateAnnouncement createAnnouncement;

    @PostMapping(value = "/secured/createAnnouncement")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody CreateAnnouncementController.Request request)
        throws DomainException, ParseException, JsonProcessingException {

        LOG.info("Create Announcement Request : [{}]", request);

        CreateAnnouncement.Output output = this.createAnnouncement.execute(
            new CreateAnnouncement.Input(request.announcementTitle,
                                         request.announcementDetail,
                                         Instant.parse(request.announcementDate)));

        Response response = new Response(
            output.created());

        LOG.info("Create Announcement Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull @NotBlank
        @JsonProperty("announcementTitle")
        String announcementTitle,

        @NotNull @NotBlank
        @JsonProperty("announcementDetail")
        String announcementDetail,

        @NotNull @NotBlank
        @JsonProperty("announcementDate")
        String announcementDate
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("created")
        boolean created
    ) implements Serializable { }

}
