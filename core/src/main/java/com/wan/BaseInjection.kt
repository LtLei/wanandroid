package com.wan

import android.app.Application
import com.wan.core.network.RetrofitClient

/**
 * 手动依赖注入
 *
 * dagger 是个好东西，但不是必须使用它。 (=_=!)
 */
object BaseInjection {
    private var application: Application? = null

    fun init(app: Application) {
        if (application != null) throw IllegalStateException("Do not init app twice!")
        application = app
    }

    fun provideApp(): Application {
        return requireNotNull(application, { "Application not initialized." })
    }

    fun provideRetrofitClient(): RetrofitClient {
        return RetrofitClient.getInstance()
    }

    fun <T> provideApi(clazz: Class<T>): T {
        return provideRetrofitClient().api(clazz)
    }
}