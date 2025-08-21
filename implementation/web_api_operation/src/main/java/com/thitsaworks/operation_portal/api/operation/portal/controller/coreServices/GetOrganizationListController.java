package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetOrganizationList;
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
public class GetOrganizationListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetOrganizationListController.class);

    private final GetOrganizationList getOrganizationList;

    @GetMapping(value = "/secured/getOrganizationList")
    public ResponseEntity<Response> execute() throws DomainException {

        LOG.info("Get Organization List Request : [{}]", "");

        var input = new GetOrganizationList.Input();
        var output = this.getOrganizationList.execute(input);

        List<Response.OrganizationInfo> organizationInfoList = new ArrayList<>();

        for (var organizationInfo : output.organizationInfoList()) {
            organizationInfoList.add(new Response.OrganizationInfo(organizationInfo.participantId().toString(),
                                                                  organizationInfo.participantName()));
        }

        var response = new Response(organizationInfoList);

        LOG.info("Get Organization List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);



    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<OrganizationInfo> organizationInfoList) {
            public record OrganizationInfo(String participantId,
                                           String participantName){}
        }
    }


