package com.thitsaworks.operation_portal.component.misc.retrofit;

import lombok.Getter;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

public final class RetrofitRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitRunner.class);

    private RetrofitRunner() { }

    /**
     * Executes a synchronous HTTP request using a given service, request payload,
     * invocation logic, and error decoder. Validates the response and, in case of a
     * failure, decodes the error response into a meaningful object or exception.
     *
     * @param <SERVICE>    The type of the service interface being invoked.
     * @param <REQ>        The type of the request payload.
     * @param <RES>        The type of the response payload.
     * @param <E>          The type of the error response represented after decoding.
     * @param service      An instance of the service to make the HTTP call.
     * @param request      The request payload to be sent in the HTTP call.
     * @param invocation   An invocation handler responsible for invoking the service with the request.
     * @param errorDecoder A decoder to transform error responses into domain-specific error objects.
     * @return A Response object containing the success response payload.
     * @throws InvocationException when the HTTP response is unsuccessful or an error occurs during invocation.
     */
    public static <SERVICE, REQ, RES, E> Response<RES> invoke(SERVICE service, REQ request, Invocation<SERVICE, REQ, RES> invocation,
                                                              ErrorDecoder<E> errorDecoder) throws InvocationException {

        try {

            Call<RES> call = invocation.invoke(service, request);
            Response<RES> response = call.execute();
            LOGGER.debug("call.executed : {}", call.isExecuted());

            if (!response.isSuccessful()) {

                if (errorDecoder != null) {

                    int statusCode = response.raw().code();
                    throw new InvocationException(errorDecoder.decode(statusCode, response.errorBody()));
                    //throw new InvocationException(errorDecoder.decode(response.code(), response.errorBody()));
                } else {

                    assert response.errorBody() != null;
                    throw new InvocationException(response.errorBody().string());
                }
            }

            return response;

        } catch (Exception e) {

            if (e instanceof InvocationException) {

                throw (InvocationException) e;
            } else {

                throw new InvocationException(e);
            }

        }
    }

    public static <SERVICE, REQ, RES, E> Response<List<RES>> invokeList(SERVICE service,
                                                                        REQ request,
                                                                        Invocation<SERVICE, REQ, List<RES>> invocation,
                                                                        ErrorDecoder<E> errorDecoder)
        throws InvocationException {

        try {

            Call<List<RES>> call = invocation.invoke(service, request);
            Response<List<RES>> response = call.execute();
            LOGGER.debug("call.executed : {}", call.isExecuted());

            if (!response.isSuccessful()) {

                if (errorDecoder != null) {
                    int statusCode = response.raw().code();
                    throw new InvocationException(errorDecoder.decode(statusCode, response.errorBody()));

                } else {

                    assert response.errorBody() != null;
                    throw new InvocationException(response.errorBody().string());

                }
            }

            return response;

        } catch (Exception e) {

            if (e instanceof InvocationException) {

                throw (InvocationException) e;

            } else {

                throw new InvocationException(e);
            }
        }
    }

    public interface ErrorDecoder<E> {

        E decode(int status, ResponseBody errorResponseBody);

    }

    public interface Invocation<SERVICE, I, O> {

        Call<O> invoke(SERVICE service, I request);

    }

    @Getter
    public static class InvocationException extends Exception {

        private Object errorResponse;

        public InvocationException(Object errorResponse) {

            super();
            this.errorResponse = errorResponse;
        }

        public InvocationException(Exception cause) {

            super(cause);
        }

    }

}
