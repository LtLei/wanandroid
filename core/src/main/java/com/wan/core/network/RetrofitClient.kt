package com.wan.core.network

import androidx.collection.ArrayMap
import com.wan.core.BuildConfig
import com.wan.core.constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.net.Proxy

/**
 * the retrofit client.
 */
class RetrofitClient private constructor() {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor =
                        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                            override fun log(message: String) {
                                Timber.d(message)
                            }
                        })
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                    addInterceptor(loggingInterceptor)
                } else {
                    proxy(Proxy.NO_PROXY)
                }
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val apis by lazy { ArrayMap<Class<*>, Any>() }

    @Suppress("UNCHECKED_CAST")
    fun <T> api(clazz: Class<T>): T {
        val cachedService = apis[clazz]
        if (cachedService != null) {
            return cachedService as T
        }
        val service = retrofit.create(clazz)
        apis[clazz] = service
        return service
    }

    companion object {
        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            return instance ?: synchronized(RetrofitClient::class.java) {
                instance ?: RetrofitClient().also {
                    instance = it
                }
            }
        }
    }
}