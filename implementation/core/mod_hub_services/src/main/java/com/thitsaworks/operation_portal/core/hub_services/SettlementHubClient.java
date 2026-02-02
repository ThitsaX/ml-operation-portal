package com.thitsaworks.operation_portal.core.hub_services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformationResponse;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRunner;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitServiceBuilder;
import com.thitsaworks.operation_portal.component.misc.retrofit.converter.NullOrEmptyConverterFactory;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.error.HubApiErrorDecoder;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.services.HubService;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.ConnectException;
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
                                                                                          .withTimeouts(120, 120, 120)
                                                                                          .build();

        this.hubApiErrorDecoder = new HubApiErrorDecoder(objectMapper);

    }

    public PostCloseSettlementWindows.Response closeSettlementWindows(int windowsId,

                                                                      PostCloseSettlementWindows.Request request)
        throws HubServicesException {

        PostCloseSettlementWindows.Response response =new PostCloseSettlementWindows.Response();

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postCloseSettlementWindows(windowsId, request),
                                             this.hubApiErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation
                    errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

              response.setErrorInformation(errorInformation);
              return  response;



            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.description(e.getMessage()));

            }
        }
        return response;
    }

    public List<GetSettlementWindows.SettlementWindow> getSettlementWindowsList(String fromDate,
                                                                                String toDate,
                                                                                String currency,
                                                                                String state,
                                                                                Integer participantId,
                                                                                GetSettlementWindows.Request request)
        throws HubServicesException {

        List<GetSettlementWindows.SettlementWindow> response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.getSettlementWindows(fromDate,
                                                                              toDate,
                                                                              currency,
                                                                              state,
                                                                              participantId),
                                             this.hubApiErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation
                    errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.code(errorInformation.getErrorCode())
                                                                                        .description(errorInformation.getErrorDescription()));

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.description(e.getMessage()));

            }
        }
        return response;

    }

    public GetSettlementWindows.SettlementWindow getSettlementWindowById(SettlementWindowId settlementWindowId)
        throws HubServicesException {

        GetSettlementWindows.SettlementWindow response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             null,
                                             (s, r) -> s.getSettlementWindows(settlementWindowId.getId()),
                                             this.hubApiErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.code(errorInformation.getErrorCode())
                                                                                        .description(errorInformation.getErrorDescription()));

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.description(e.getMessage()));

            }
        }
        return response;

    }

    public PostCreateSettlement.Response createSettlement(PostCreateSettlement.Request request)
        throws HubServicesException {

        PostCreateSettlement.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.postCreateSettlement(request),
                                             this.hubApiErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.code(errorInformation.getErrorCode())
                                                                                 .description(errorInformation.getErrorDescription()));

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.description(e.getMessage()));

            }
        }
        return response;
    }

    public Settlement getSettlement(Integer settlementId)
        throws HubServicesException {

        Settlement settlement;

        try {

            settlement = RetrofitRunner.invoke(this.hubService,
                                               null,
                                               (s, r) -> s.getSettlementById(settlementId),
                                               this.hubApiErrorDecoder)
                                       .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.code(errorInformation.getErrorCode())
                                                                                 .description(errorInformation.getErrorDescription()));

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.description(e.getMessage()));

            }
        }
        return settlement;
    }

    public List<Settlement> getSettlementList(String currency,
                                              Integer participantId,
                                              Integer settlementWindowId,
                                              Integer accountId,
                                              String state,
                                              String fromDateTime,
                                              String toDateTime,
                                              String fromSettlementWindowDateTime,
                                              String toSettlementWindowDateTime)

        throws HubServicesException {

        List<Settlement> settlementList;

        try {

            settlementList = RetrofitRunner.invoke(this.hubService, null,
                                                   (s, r) -> s.getSettlements(currency,
                                                                              participantId,
                                                                              settlementWindowId,
                                                                              accountId,
                                                                              state,
                                                                              fromDateTime,
                                                                              toDateTime,
                                                                              fromSettlementWindowDateTime,
                                                                              toSettlementWindowDateTime),
                                                   this.hubApiErrorDecoder)
                                           .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.code(errorInformation.getErrorCode())
                                                                                 .description(errorInformation.getErrorDescription()));

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.description(e.getMessage()));

            }
        }
        return settlementList;
    }

    public PutUpdateSettlement.Response putUpdateSettlement(Integer settlementId, PutUpdateSettlement.Request request)
        throws HubServicesException {

        PutUpdateSettlement.Response response;

        try {

            response = RetrofitRunner.invoke(this.hubService,
                                             request,
                                             (s, r) -> s.putUpdateSettlement(settlementId, request),
                                             this.hubApiErrorDecoder)
                                     .body();

        } catch (RetrofitRunner.InvocationException e) {

            if (e.getErrorResponse() != null && e.getErrorResponse() instanceof ErrorInformationResponse) {

                ErrorInformation errorInformation =
                    ((ErrorInformationResponse) e.getErrorResponse()).getErrorInformation();

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.code(errorInformation.getErrorCode())
                                                                                 .description(errorInformation.getErrorDescription()));

            } else if (e.getCause() instanceof ConnectException) {

                throw new HubServicesException(HubServicesErrors.CONNECTION_ERROR);

            } else {

                throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.description(e.getMessage()));

            }
        }
        return response;
    }

}
