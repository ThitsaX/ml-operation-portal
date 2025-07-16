package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.GetParticipantContactList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantContactListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantContactListController.class);

    private final GetParticipantContactList getParticipantContactList;


    @GetMapping("/secured/getParticipantContactList")
    public ResponseEntity<Response> execute(
        @RequestParam("participantId") String participantId) throws DomainException, JsonProcessingException {

        LOG.info("Get participant contact list request : participantId = {}", participantId);

        GetParticipantContactList.Output output = this.getParticipantContactList.execute(
            new GetParticipantContactList.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ContactInfo> contactInfoList = new ArrayList<>();

        for (var contact : output.contactInfoList()) {
            contactInfoList.add(new Response.ContactInfo(contact.contactId().getId().toString(),
                                                         contact.name(),
                                                         contact.title(),
                                                         contact.email().getValue(),
                                                         contact.mobile().getValue(),
                                                         contact.contactType().toString()));
        }

        return ResponseEntity.ok(new Response(contactInfoList));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<Response.ContactInfo> contactInfoList) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ContactInfo(String contactId,
                                  String name,
                                  String title,
                                  String email,
                                  String mobile,
                                  String contactType) { }
    }
}
