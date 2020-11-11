package com.lcz.bm

import android.app.Application
import com.didichuxing.doraemonkit.DoraemonKit
import com.didichuxing.doraemonkit.kit.AbstractKit
import dagger.hilt.android.HiltAndroidApp

/**
 * desc: TODO
 *
 *
 * create by Arrow on 2020-11-05
 */
@HiltAndroidApp
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val mapKits: LinkedHashMap<String, MutableList<AbstractKit>> = linkedMapOf()
        DoraemonKit.install(this,mapKits,"pid")
    }
}