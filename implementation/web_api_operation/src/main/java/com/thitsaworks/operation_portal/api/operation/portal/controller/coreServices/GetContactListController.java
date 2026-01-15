package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetContactList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetContactListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetContactListController.class);

    private final GetContactList getContactList;

    private final ObjectMapper objectMapper;

    private final List<String> contactTypeOrder = List.of("TECHNICAL",
                                                          "BUSINESS",
                                                          "LEVEL1",
                                                          "LEVEL2",
                                                          "LEVEL3",
                                                          "LEVEL4");

    @GetMapping(value = "/secured/getContactList")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException, JsonProcessingException {

        LOG.info("Get Contact List Request : [{}]", participantId);

        var
            output =
            this.getContactList.execute(new GetContactList.Input(new ParticipantId(Long.parseLong(participantId))));

        var response = new Response(output.contactInfoList()
                                          .stream()
                                          .sorted(Comparator.comparing(
                                                                (GetContactList.Output.ContactInfo c) -> this.contactTypeOrder.indexOf(c.contactType()))
                                                            .thenComparing(
                                                                GetContactList.Output.ContactInfo::name,
                                                                Comparator.nullsLast(String::compareToIgnoreCase)))
                                          .map(contact -> new Response.ContactInfo(contact.contactId()
                                                                                          .getEntityId()
                                                                                          .toString(),
                                                                                   contact.name(),
                                                                                   contact.position(),
                                                                                   contact.email() != null ?
                                                                                       contact.email()
                                                                                              .getValue() : null,
                                                                                   contact.mobile() != null ?
                                                                                       contact.mobile()
                                                                                              .getValue() : null,
                                                                                   contact.contactType()))
                                          .toList());

        LOG.info("Get Contact List Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<ContactInfo> contactInfoList) implements Serializable {

        public record ContactInfo(@JsonProperty("contactId") String contactId,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("position") String position,
                                  @JsonProperty("email") String email,
                                  @JsonProperty("mobile") String mobile,
                                  @JsonProperty("contactType") String contactType) implements Serializable {
        }

    }

}
