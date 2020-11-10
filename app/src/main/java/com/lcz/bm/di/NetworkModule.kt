package com.lcz.bm.di

import com.lcz.bm.api.LczBMService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideLczBMService(): LczBMService {
        return LczBMService.create()
    }
}