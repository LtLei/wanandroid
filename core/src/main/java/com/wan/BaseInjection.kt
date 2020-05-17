package com.wan

import com.wan.core.network.RetrofitClient

/**
 * 手动依赖注入
 *
 * dagger 是个好东西，但是最好不要使用它。 (=_=!)
 */
object BaseInjection {
    fun <T> provideApi(clazz: Class<T>): T {
        return RetrofitClient.getInstance().api(clazz)
    }
}