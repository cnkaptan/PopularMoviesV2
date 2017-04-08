package com.cnkaptan.transferwisehomework;

import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cnkaptan on 08/04/2017.
 */
@Module
public class AppModule {

    @NonNull
    private final Context mContext;

    public AppModule(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    Context provideContext(){
        return this.mContext;
    }
}
