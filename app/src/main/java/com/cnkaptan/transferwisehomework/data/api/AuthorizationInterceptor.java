package com.cnkaptan.transferwisehomework.data.api;

import com.cnkaptan.transferwisehomework.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public final class AuthorizationInterceptor implements Interceptor {
    private static final String API_KEY_PARAM = "api_key";
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final HttpUrl originalUrl = originalRequest.url();

        final HttpUrl newHttpUrl = originalUrl.newBuilder()
                .addQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        final Request newRequest = originalRequest.newBuilder()
                .url(newHttpUrl)
                .build();

        return chain.proceed(newRequest);
    }
}
