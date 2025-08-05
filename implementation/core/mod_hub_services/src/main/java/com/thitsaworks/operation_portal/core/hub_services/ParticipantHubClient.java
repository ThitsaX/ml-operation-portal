package com.thitsaworks.operation_portal.core.hub_services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRunner;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitServiceBuilder;
import com.thitsaworks.operation_portal.component.misc.retrofit.converter.NullOrEmptyConverterFactory;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.api.PutParticipantStatus;
import com.thitsaworks.operation_portal.core.hub_services.error.HubErrorDecoder;
import com.thitsaworks.operation_portal.core.hub_services.error.HubErrorResponse;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.services.HubService;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.ConnectException;

@Component
public class ParticipantHubClient {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantHubClient.class);

    private final HubServicesConfiguration.Settings settings;

    private final HubService hubService;

    private final HubErrorDecoder hubErrorDecoder;

    @Autowired
    public ParticipantHubClient(HubServicesConfiguration.Settings settings) {

        this.settings = settings;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        this.hubService = new RetrofitServiceBuilder<>(HubService.class,
                                                       this.settings.centralLedgerEndpoint()).withHttpLogging(
                                                                                                 HttpLoggingInterceptor.Level.BODY,
                                                                                                 true)
                                                                                             .withConverterFactories(new NullOrEmptyConverterFactory(),
                                                                                                                     ScalarsConverterFactory.create(),
                                                                                                                     JacksonConverterFactory.create(
                                                                                                                         objectMapper))
                                                                                             .build();

        this.hubErrorDecoder = new HubErrorDecoder(objectMapper);

    }

    public PostParticipantBalance.Response postParticipantBalance(String participantId,
                                                                  String accountId,
                                                                  PostParticipantBalance.Request request)
        throws HubServicesException, ConnectException {

        PostParticipantBalance.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postParticipantBalance(participantId, accountId, request),
                                             this.hubErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof HubErrorResponse) {

                //TODO: To implement error handling with proper error response format
                throw new HubServicesException(null);

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

    public PutParticipantStatus.Response putParticipantStatus(PutParticipantStatus.Request request)
        throws HubServicesException, ConnectException {

        PutParticipantStatus.Response response;

        try {


            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.putParticipantStatus(request.participantName(),
                                                                              request.participantCurrencyId(), new RequestToHub(
                                                     request.isActive())),
                                             this.hubErrorDecoder)
                                     .body();


        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof HubErrorResponse) {

                //TODO: To implement error handling with proper error response format
                throw new HubServicesException(null);

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;


    }

    public GetParticipant.Response getParticipant(GetParticipant.Request request)
        throws HubServicesException, ConnectException {

        GetParticipant.Response response;

        try {


            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.getParticipant(request.participantName()
                                                                              ),
                                             this.hubErrorDecoder)
                                     .body();


        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof HubErrorResponse) {

                //TODO: To implement error handling with proper error response format
                throw new HubServicesException(null);

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

   public record RequestToHub(

        boolean isActive
    ) {
    }

}
