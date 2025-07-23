package com.thitsaworks.operation_portal.component.misc.retrofit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Map;

public class RetrofitServiceBuilder<S> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitServiceBuilder.class);

    private final Class<S> service;

    private final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    private final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

    public RetrofitServiceBuilder(Class<S> service, String baseUrl) {

        this.service = service;
        this.retrofitBuilder.baseUrl(baseUrl);

    }

    public S build() {

        Retrofit retrofit = this.retrofitBuilder.client(this.httpClientBuilder.build())
                                                .build();

        return retrofit.create(this.service);
    }

    public RetrofitServiceBuilder<S> withConverterFactories(Converter.Factory... factories) {

        if (factories != null) {

            for (Converter.Factory factory : factories) {

                this.retrofitBuilder.addConverterFactory(factory);
            }
        }

        return this;
    }

    public RetrofitServiceBuilder<S> withCustomHeaders(Map<String, String> headers) {

        this.httpClientBuilder.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder requestBuilder = null;
            requestBuilder = original.newBuilder();

            if (headers != null) {

                for (String key : headers.keySet()) {

                    requestBuilder.header(key, headers.get(key));

                }

            }

            Request request = requestBuilder.build();
            return chain.proceed(request);

        });

        return this;
    }

    public RetrofitServiceBuilder<S> withDisableSSLVerification() {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustManager = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                    return new java.security.cert.X509Certificate[]{};

                }

            }};

        // Install the all-trusting trust manager
        final SSLContext sslContext;

        try {

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            this.httpClientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManager[0]);
            this.httpClientBuilder.hostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {

                    return true;

                }

            });

        } catch (KeyManagementException | NoSuchAlgorithmException e) {

            throw new RuntimeException(e);

        }

        return this;
    }

    public RetrofitServiceBuilder<S> withHttpLogging(HttpLoggingInterceptor.Level level, boolean info) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
            if (info) {
                LOGGER.info("Retrofit : {}", message);
            } else {
                LOGGER.debug("Retrofit :{}", message);
            }
        });

        logging.setLevel(level);

        this.httpClientBuilder.addInterceptor(logging);

        return this;
    }

    public RetrofitServiceBuilder<S> withInterceptors(Interceptor... interceptors) {

        if (interceptors != null) {

            for (Interceptor interceptor : interceptors) {

                this.httpClientBuilder.addInterceptor(interceptor);
            }
        }

        return this;
    }

    public RetrofitServiceBuilder<S> withTimeouts(int connectTimeout, int callTimeout, int readTimeout) {

        this.httpClientBuilder.connectTimeout(Duration.ofSeconds(connectTimeout <= 0 ? 60 : connectTimeout));
        this.httpClientBuilder.callTimeout(Duration.ofSeconds(callTimeout <= 0 ? 60 : callTimeout));
        this.httpClientBuilder.readTimeout(Duration.ofSeconds(readTimeout <= 0 ? 60 : readTimeout));

        return this;
    }

}
