package com.wan

import android.app.Application
import com.wan.data.user.UserManager
import timber.log.Timber

class WanAndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        BaseInjection.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // 确保UserManager初始化完成
        UserManager.getInstance()
    }
}