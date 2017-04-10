package com.cnkaptan.transferwisehomework.di;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cnkaptan.transferwisehomework.AppModule;
import com.cnkaptan.transferwisehomework.BuildConfig;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.api.AuthorizationInterceptor;
import com.cnkaptan.transferwisehomework.data.api.MovieApi;
import com.cnkaptan.transferwisehomework.data.database.realm.RealmDataSource;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cnkaptan on 08/04/2017.
 */
@Module(includes = AppModule.class)
public class ApiModule {
    @Nullable
    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static final String CACHE_DIR = "HttpResponseCache";
    private static final long CACHE_SIZE = 10 * 1024 * 1024;    // 10 MB
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;
    private static final String NORMAL_HTTP_CLIENT = "normalHttpClient";
    private static final String LOG_HTTP_CLIENT = "logHttpClient";

    @Provides
    @Singleton
    String provideBaseUrl() {
        return BASE_URL;
    }

    @Provides
    @Singleton
    @Named(LOG_HTTP_CLIENT)
    OkHttpClient provideLogOkHttpClient(@NonNull Context context) {
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }
        okHttpClientBuilder
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new AuthorizationInterceptor());

        final File baseDir = context.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, CACHE_DIR);
            okHttpClientBuilder.cache(new Cache(cacheDir, CACHE_SIZE));
        }
        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    @Named(NORMAL_HTTP_CLIENT)
    OkHttpClient provideOkHttpClient(@Nullable Context context) {
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new AuthorizationInterceptor());
        final File baseDir = context.getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, CACHE_DIR);
            okHttpClientBuilder.cache(new Cache(cacheDir, CACHE_SIZE));
        }
        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    @NonNull
    Retrofit provideRetrofit(@NonNull String baseUrl, @Named(LOG_HTTP_CLIENT) OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    MovieApi provideApi(Retrofit retrofit){
        return retrofit.create(MovieApi.class);
    }

    @Provides
    @Singleton
    RealmDataSource provideDataSource(@NonNull Context context){
        return new RealmDataSource(context);

    }

    @Provides
    @Singleton
    DataManager provideDataManager(MovieApi movieApi, RealmDataSource realmDataSource){
        return new DataManager(movieApi,realmDataSource);
    }
}
