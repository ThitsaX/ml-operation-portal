package com.thitsaworks.operation_portal.core.hub_services.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformationResponse;
import com.thitsaworks.operation_portal.component.misc.retrofit.RetrofitRunner;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;

import java.io.IOException;

@RequiredArgsConstructor
public class HubApiErrorDecoder implements RetrofitRunner.ErrorDecoder<ErrorInformationResponse> {

    private final ObjectMapper objectMapper;

    @Override
    public ErrorInformationResponse decode(int status, ResponseBody errorResponseBody) {

        try {

            return this.objectMapper.readValue(errorResponseBody.string(), ErrorInformationResponse.class);

        } catch (IOException e) {

            ErrorInformation errorInformation = new ErrorInformation();

            errorInformation.setErrorCode("-1111");

            errorInformation.errorDescription("Something went wrong.");

            return new ErrorInformationResponse().errorInformation(errorInformation);

        }

    }

}
