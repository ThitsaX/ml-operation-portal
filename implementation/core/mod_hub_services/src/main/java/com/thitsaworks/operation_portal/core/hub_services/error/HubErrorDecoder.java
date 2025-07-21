package com.thitsaworks.operation_portal.core.hub_services.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRunner;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;

import java.io.IOException;

@RequiredArgsConstructor
public class HubErrorDecoder implements RetrofitRunner.ErrorDecoder<HubErrorResponse> {

    private final ObjectMapper objectMapper;

    @Override
    public HubErrorResponse decode(int status, ResponseBody errorResponseBody) {

        try {

            return this.objectMapper.readValue(errorResponseBody.string(), HubErrorResponse.class);

        } catch (IOException e) {

            return new HubErrorResponse("-1111", "Something went wrong.");

        }

    }

}
