package com.thitsaworks.dfsp_portal.component.retrofit;

import lombok.Getter;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Map;

public abstract class RetrofitRestApi<SERVICE, REQUEST, RESPONSE, ERROR> {

    private static final Logger LOG = LoggerFactory.getLogger(RetrofitRestApi.class);

    private final ErrorDecoder<ERROR> errorDecoder;

    public RetrofitRestApi(ErrorDecoder<ERROR> errorDecoder) {

        assert  errorDecoder != null;

        this.errorDecoder = errorDecoder;

    }

    protected abstract Call<RESPONSE> call(REQUEST request, Map<String, Object> extra);

    public Response<RESPONSE> invoke(final REQUEST request) throws RestException {

        return this.invoke(request, null);

    }

    public Response<RESPONSE> invoke(final REQUEST request, Map<String, Object> extra) throws RestException {

        try {

            Call<RESPONSE> call = RetrofitRestApi.this.call(request, extra);

            Response<RESPONSE> response = call.execute();

            LOG.info("call.executed : {}", call.isExecuted());

            if (!response.isSuccessful()) {

                throw new RestException(this.errorDecoder.decode(response.code(), response.errorBody()));

            }

            return response;

        } catch (Exception e) {

            if (e instanceof RestException) {

                throw (RestException) e;

            } else {

                throw new RestException(e);

            }

        }

    }

    @Getter
    public static class RestException extends Exception {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        private Object errorResponse;

        public RestException(Object errorResponse) {

            super();

            this.errorResponse = errorResponse;

        }

        public RestException(Exception cause) {

            super(cause);

        }

    }

    public interface ErrorDecoder<E> {

        E decode(int status, ResponseBody errorResponseBody);

    }

}