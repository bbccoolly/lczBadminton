package com.lcz.bm.di

import android.content.Context
import com.lcz.bm.util.GsonUtil
import com.lcz.bm.util.RefreshStatusUtil
import com.lcz.bm.util.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
@InstallIn(ApplicationComponent::class)
@Module
object MainActivityModule {
    @Provides
    @Singleton
    fun providePres(@ApplicationContext appContext: Context): SharedPreferenceStorage {
        return SharedPreferenceStorage(appContext)
    }

    @Provides
    @Singleton
    fun provideGson(): GsonUtil {
        return GsonUtil()
    }

}