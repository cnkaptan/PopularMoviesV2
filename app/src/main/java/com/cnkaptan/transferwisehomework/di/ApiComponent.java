package com.cnkaptan.transferwisehomework.di;

import com.cnkaptan.transferwisehomework.AppModule;
import com.cnkaptan.transferwisehomework.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by cnkaptan on 08/04/2017.
 */
@Singleton
@Component(modules = {AppModule.class,ApiModule.class})
public interface ApiComponent {
    void inject(MainActivity mainActivity);
}
