package com.thitsaworks.operation_portal.core.hub_services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformationResponse;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRunner;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitServiceBuilder;
import com.thitsaworks.operation_portal.component.misc.retrofit.converter.NullOrEmptyConverterFactory;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.error.HubApiErrorDecoder;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesApiException;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.services.HubService;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SettlementHubClient {

    private static final Logger LOG = LoggerFactory.getLogger(SettlementHubClient.class);

    private final HubServicesConfiguration.Settings settings;

    private final HubService hubService;

    private final HubApiErrorDecoder hubApiErrorDecoder;

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

        this.hubApiErrorDecoder = new HubApiErrorDecoder(objectMapper);

    }

    public PostCloseSettlementWindows.Response closeSettlementWindows(int windowsId,

                                                                      PostCloseSettlementWindows.Request request)
            throws HubServicesException, ConnectException, HubServicesApiException {

        PostCloseSettlementWindows.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postCloseSettlementWindows(windowsId, request),
                                             this.hubApiErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation errorInformation = ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesApiException(((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation());

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

    public PostCreateSettlement.Response createSettlement(PostCreateSettlement.Request request)
            throws HubServicesException, ConnectException, HubServicesApiException {

        PostCreateSettlement.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postCreateSettlement(request),
                                             this.hubApiErrorDecoder).body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                throw new HubServicesApiException(((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation());

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

    public GetSettlement.Response getSettlement(Integer settlementId, GetSettlement.Request request)
            throws HubServicesException, ConnectException, HubServicesApiException {

        GetSettlement.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.getSettlementById(settlementId),
                                             this.hubApiErrorDecoder).body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                throw new HubServicesApiException(((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation());

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

    public List<Settlement> getSettlementsByParam(String currency,
                                                  Integer participantId,
                                                  Integer settlementWindowId,
                                                  Integer accountId,
                                                  String state,
                                                  String fromDateTime,
                                                  String toDateTime,
                                                  String fromSettlementWindowDateTime,
                                                  String toSettlementWindowDateTime,
                                                  GetSettlement.Request request)
            throws HubServicesException, ConnectException, HubServicesApiException {

        List<Settlement> settlementList;

        try {

            settlementList = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.getSettlementsByParam(currency,
                                                                               participantId,
                                                                               settlementWindowId,
                                                                               accountId,
                                                                               state,
                                                                               fromDateTime,
                                                                               toDateTime,
                                                                               fromSettlementWindowDateTime,
                                                                               toSettlementWindowDateTime),
                                             this.hubApiErrorDecoder).body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                throw new HubServicesApiException(((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation());

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return settlementList;
    }

    public PutUpdateSettlement.Response putUpdateSettlement(Integer settlementId, PutUpdateSettlement.Request request)
            throws HubServicesException, ConnectException, HubServicesApiException {

        PutUpdateSettlement.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.putUpdateSettlement(settlementId, request),
                                             this.hubApiErrorDecoder).body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                throw new HubServicesApiException(((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation());

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(null);

            }
        }
        return response;
    }

}
