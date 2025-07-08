package com.thitsaworks.operation_portal.core.hub_services.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRestApi;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import okhttp3.ResponseBody;

import java.io.IOException;

public class HubErrorDecoder implements RetrofitRestApi.ErrorDecoder<HubErrorResponse> {

    @Override
    public HubErrorResponse decode(int status, ResponseBody errorResponseBody) {

        ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

        try {

            return objectMapper.readValue(errorResponseBody.string(), HubErrorResponse.class);

        } catch (IOException e) {

            return null;

        }

    }

}
