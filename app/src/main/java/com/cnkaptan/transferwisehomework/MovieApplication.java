package com.cnkaptan.transferwisehomework;

import android.app.Application;
import android.support.annotation.NonNull;

import com.cnkaptan.transferwisehomework.di.ApiComponent;
import com.cnkaptan.transferwisehomework.di.ApiModule;
import com.cnkaptan.transferwisehomework.di.DaggerApiComponent;

/**
 * Created by cnkaptan on 08/04/2017.
 */

public class MovieApplication extends Application {
    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        apiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();

    }

    @NonNull
    public ApiComponent getApiComponent(){
        return this.apiComponent;
    }
}
