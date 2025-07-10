package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRestApi;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.hub_services.error.HubErrorResponse;
import com.thitsaworks.operation_portal.core.hub_services.services.HubService;
import retrofit2.Call;

import java.util.Map;

public class PostParticipantBalance
        extends RetrofitRestApi<HubService, PostParticipantBalance.Request, PostParticipantBalance.Response, HubErrorResponse> {

    private final String endpoint;

    HubService hubService;

    public PostParticipantBalance(String endpoint,
                                  RetrofitRestApi.ErrorDecoder<HubErrorResponse> HubErrorDecoder,
                                  HubService hubService) {

        super(HubErrorDecoder);
        this.endpoint = endpoint;
        this.hubService = hubService;
    }

    @Override
    protected Call<Response> call(Request request, Map<String, Object> extra) throws JsonProcessingException {

        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

        String json = objectMapper.writeValueAsString(request);

        return this.hubService.postParticipantBalance(this.endpoint, json);
    }

    public record Request(
            @JsonProperty("transferId")
            String transferId,

            @JsonProperty("externalReference")
            String externalReference,

            @JsonProperty("action")
            String action,

            @JsonProperty("reason")
            String reason,

            @JsonProperty("amount")
            Money amount) {
    }

    public record Response(
            @JsonProperty("accessKey") String accessKey,

            @JsonProperty("secretKey") String secretKey) {

    }

}

