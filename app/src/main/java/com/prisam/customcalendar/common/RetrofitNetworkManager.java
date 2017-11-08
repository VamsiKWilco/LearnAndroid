package com.prisam.customcalendar.common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vamsi on 7/22/16.
 */
public final class RetrofitNetworkManager {

    private static volatile RetrofitNetworkManager instance;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder;

    private RetrofitNetworkManager() {
    }

    public static RetrofitNetworkManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitNetworkManager.class) {
                if (instance == null) {
                    instance = new RetrofitNetworkManager();
                }
            }
        }
        return instance;
    }


    public <S> S createService(Class<S> serviceClass, String baseURL){
        //TODO: add support for params.. maybe..
        //TODO: add try/catch
        httpClient.interceptors().clear();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

        builder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create());

        httpClient.connectTimeout(200, TimeUnit.SECONDS);
        httpClient.readTimeout(200, TimeUnit.SECONDS);
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient client = httpClient.build();
        /*builder.baseUrl("").build();*/
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

}
