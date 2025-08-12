package com.thitsaworks.operation_portal.core.hub_services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRunner;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitServiceBuilder;
import com.thitsaworks.operation_portal.component.misc.retrofit.converter.NullOrEmptyConverterFactory;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
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
public class SettlementHubClient {

    private static final Logger LOG = LoggerFactory.getLogger(SettlementHubClient.class);

    private final HubServicesConfiguration.Settings settings;

    private final HubService hubService;

    private final HubErrorDecoder hubErrorDecoder;

    @Autowired
    public SettlementHubClient(HubServicesConfiguration.Settings settings) {

        this.settings = settings;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        this.hubService = new RetrofitServiceBuilder<>(HubService.class,
                                                       this.settings.settlementEndpoint()).withHttpLogging(
                                                                                                 HttpLoggingInterceptor.Level.BODY,
                                                                                                 true)
                                                                                             .withConverterFactories(new NullOrEmptyConverterFactory(),
                                                                                                                     ScalarsConverterFactory.create(),
                                                                                                                     JacksonConverterFactory.create(
                                                                                                                         objectMapper))
                                                                                             .build();

        this.hubErrorDecoder = new HubErrorDecoder(objectMapper);

    }

    public PostCloseSettlementWindows.Response closeSettlementWindows(int windowsId,

                                                                      PostCloseSettlementWindows.Request request)
        throws HubServicesException, ConnectException {

        PostCloseSettlementWindows.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postCloseSettlementWindows(windowsId, request),
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

    public PostCreateSettlement.Response createSettlement(PostCreateSettlement.Request request)
            throws HubServicesException, ConnectException {

        PostCreateSettlement.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postCreateSettlement(request),
                                             this.hubErrorDecoder).body();

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

}
