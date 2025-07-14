package com.thitsaworks.operation_portal.core.hub_services;

import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRestApi;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitService;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.error.HubErrorDecoder;
import com.thitsaworks.operation_portal.core.hub_services.error.HubErrorResponse;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServiceErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.services.HubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;

@Component
public class HubClient {

    private static final Logger LOG = LoggerFactory.getLogger(HubClient.class);

    @Autowired
    private HubServicesConfiguration.Settings settings;

    private final HubService hubService;

    public HubClient() {

        this.hubService =
                new RetrofitService<>(HubService.class, "http://localhost:4001", null, true).getService();

    }

    public PostParticipantBalance.Response postParticipantBalance(String participantId,
                                                                  String accountId,
                                                                  PostParticipantBalance.Request request)
            throws HubServicesException, ConnectException {

        String endpoint = String.format("/participants/%s/accounts/%s", participantId, accountId);

        PostParticipantBalance postParticipantBalance =
                new PostParticipantBalance(
                        this.settings.hubEndpoint() + endpoint,
                        new HubErrorDecoder(), this.hubService);

        PostParticipantBalance.Response response;

        try {

            response = postParticipantBalance.invoke(request).body();

        } catch (RetrofitRestApi.RestException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof HubErrorResponse) {

                //TODO: To implement error handling with proper error response format
                throw new HubServicesException(null);

            } else if (e.getCause() instanceof ConnectException) {

                throw new ConnectException(e.getMessage());
            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

}
