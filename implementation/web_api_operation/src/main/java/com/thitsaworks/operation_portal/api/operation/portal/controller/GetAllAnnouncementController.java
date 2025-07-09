package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.GetAllAnnouncement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetAllAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementController.class);

    private final GetAllAnnouncement getAllAnnouncement;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/public/getAnnouncements")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {


        GetAllAnnouncement.Output output = this.getAllAnnouncement.execute(new GetAllAnnouncement.Input());

        List<Response.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (var announcement : output.announcementInfoList()) {

            LocalDateTime announcementDateTime = LocalDateTime.ofInstant(announcement.announcementDate(), ZoneId.systemDefault());

            announcementInfoList.add(new Response.AnnouncementInfo(announcement.announcementId().getId().toString(),
                                                                   announcement.announcementTitle(),
                                                                   announcement.announcementDetail(),
                                                                   announcementDateTime.format(formatter)));
        }

        if (!announcementInfoList.isEmpty()) {

            announcementInfoList.sort(Comparator.comparing(Response.AnnouncementInfo::announcementDate).reversed());

        }
        Response response = new Response(announcementInfoList);

        LOG.info("Get announcements response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("announcementInfoList") List<AnnouncementInfo> announcementInfoList)
            implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record AnnouncementInfo(
                @JsonProperty("id") String announcementId,
                @JsonProperty("title") String announcementTitle,
                @JsonProperty("detail") String announcementDetail,
                @JsonProperty("date") String announcementDate) implements Serializable {
        }
    }
}
