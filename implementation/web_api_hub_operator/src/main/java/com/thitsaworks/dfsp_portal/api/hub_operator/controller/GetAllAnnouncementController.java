package com.thitsaworks.dfsp_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.usecase.hub_operator.GetAllAnnouncement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class GetAllAnnouncementController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllAnnouncementController.class);

    @Autowired
    private GetAllAnnouncement getAllAnnouncement;

    @RequestMapping(value = "/public/get_all_announcement", method = RequestMethod.GET)
    public ResponseEntity<Response> execute() throws DFSPPortalException {

        GetAllAnnouncement.Output output = this.getAllAnnouncement.execute(new GetAllAnnouncement.Input());

        List<Response.AnnouncementInfo> announcementInfoList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (var announcement : output.getAnnouncementInfoList()) {

            LocalDateTime announcementDateTime = LocalDateTime.ofInstant(announcement.getAnnouncementDate(), ZoneId.systemDefault());

            announcementInfoList.add(new Response.AnnouncementInfo(announcement.getAnnouncementId().getId().toString(),
                                                                   announcement.getAnnouncementTitle(),
                                                                   announcement.getAnnouncementDetail(),
                                                                   announcementDateTime.format(formatter)));
        }

        if (announcementInfoList.size() > 0) {

            announcementInfoList.sort(Comparator.comparing(Response.AnnouncementInfo::getAnnouncementDate).reversed());

        }

        return new ResponseEntity<>(new Response(announcementInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("announcement_info_list")
        protected List<AnnouncementInfo> announcementInfoList;

        @Getter
        @AllArgsConstructor
        public static class AnnouncementInfo implements Serializable {

            @JsonProperty("id")
            protected String announcementId;

            @JsonProperty("title")
            protected String announcementTitle;

            @JsonProperty("detail")
            protected String announcementDetail;

            @JsonProperty("date")
            protected String announcementDate;

        }
    }
}
