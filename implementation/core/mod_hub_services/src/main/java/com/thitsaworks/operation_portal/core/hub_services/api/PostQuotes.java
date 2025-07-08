package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.mojaloop.thitsaconnect.component.retrofit.RetrofitRestApi;
import com.thitsaworks.mojaloop.thitsaconnect.fspiop.model.*;
import com.thitsaworks.mojaloop.thitsaconnect.thitsaconnect.to.hub.services.FspiopService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import retrofit2.Call;

import java.io.Serializable;
import java.util.Map;

public class PostQuotes
        extends RetrofitRestApi<FspiopService, QuotesPostRequest, PostQuotes.Response, ErrorInformationResponse> {

    private final FspiopService fspiopiopService;

    private final String endpoint;

    private final String fsiopEncryption;

    private final String uri;

    private final String httpMethod;

    private final String fspiopSource;

    private final String fspiopDestination;

    private final String fsiopSignature;

    //headers
    private final String fspiopDate ;

    private final String accept = "application/vnd.interoperability.quotes+json;version=1.1";

    private final String contentType = "application/vnd.interoperability.quotes+json;version=1.1";

    public PostQuotes(FspiopService fspiopService, String endpoint, String fspiopSource, String fspiopDestination,
                      String fspiopDate,
                      String fsiopSignature,
                      String uri,
                      String httpMethod,
                      ErrorDecoder<ErrorInformationResponse> exceptionDecoder, String fsiopEncryption) {

        super(exceptionDecoder);

        this.fspiopiopService = fspiopService;
        this.endpoint = endpoint;
        this.fspiopSource = fspiopSource;
        this.fspiopDestination = fspiopDestination;
        this.fsiopSignature = fsiopSignature;
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.fsiopEncryption = fsiopEncryption;
        this.fspiopDate=fspiopDate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @JsonProperty("quoteId")
        String quoteId;

        @JsonProperty("transactionId")
        String transactionId;

        @JsonProperty("amountType")
        AmountType amountType;

        @JsonProperty("amount")
        Money amount;

        @JsonProperty("payer")
        Payer payer;

        @JsonProperty("payee")
        Payee payee;

        @JsonProperty("transactionType")
        TransactionType transactionType;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Payer {

            @JsonProperty("partyIdInfo")
            PartyIdInfo partyIdInfo;

        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Payee {

            @JsonProperty("partyIdInfo")
            PartyIdInfo partyIdInfo;

        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TransactionType {

            @JsonProperty("scenario")
            TransactionScenario scenario;

            @JsonProperty("initiator")
            TransactionInitiator initiator;

            @JsonProperty("initiatorType")
            TransactionInitiatorType initiatorType;

        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("status")
        String status;

    }

    @Override
    protected Call<Response> call(QuotesPostRequest request, Map<String, Object> extra) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        String cleanRequest = mapper.writeValueAsString(request);

        return this.fspiopiopService.postQuotes(this.endpoint, this.fspiopSource, this.fspiopDestination,
                this.fspiopDate, this.fsiopSignature, this.uri, this.httpMethod, this.fsiopEncryption,this.accept, this.contentType, cleanRequest);

    }

}
