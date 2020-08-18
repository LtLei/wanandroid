package com.wan.core.extensions

import android.net.ParseException
import androidx.room.RoomDatabase
import com.wan.core.Resource
import com.wan.core.constant.*
import com.wan.core.network.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * 当请求发生 [com.wan.core.network.ApiResponse] 之外的错误时，将其转为 [com.wan.core.Resource]
 */
fun <T> Throwable.toResource(): Resource<T> {
    return when (this) {
        is HttpException -> Resource.error(code(), message())

        is ConnectException, is UnknownHostException ->
            Resource.error(CONNECT_ERROR_CODE, CONNECT_ERROR)

        is InterruptedIOException -> Resource.error(CONNECT_TIMEOUT_CODE, CONNECT_TIMEOUT)

        is SerializationException, is JSONException, is ParseException ->
            Resource.error(DATA_ERROR_CODE, DATA_ERROR)

        else -> Resource.error(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR)
    }
}

fun <T> ApiResponse<T>.toResource(block: (ApiResponse<T>) -> Resource<T>): Resource<T> {
    return if (this.isSuccess()) {
        block(this)
    } else {
        Resource.error(errorCode, errorMsg ?: DEFAULT_API_ERROR)
    }
}

fun <T, R> Resource<T>.copyTo(block: (T?) -> R?): Resource<R> {
    return Resource(state, block.invoke(data), code, msg)
}

inline fun <R> safeCall(block: () -> Resource<R>): Resource<R> {
    return try {
        block()
    } catch (e: Throwable) {
        Timber.e(e, "safeCall error!")
        e.toResource()
    }
}

/**
 * for ViewModelProvider.Factory
 */
inline fun <T> requireClassIsT(
    modelClass: Class<T>,
    clazz: Class<*>,
    block: () -> T
): T {
    if (!modelClass.isAssignableFrom(clazz)) {
        throw IllegalArgumentException("not a ${clazz}.")
    }
    return block()
}


@Suppress("DEPRECATION")
suspend fun <T> RoomDatabase.suspendRunInTransaction(block: suspend () -> T) {
    withContext(Dispatchers.IO) {
        beginTransaction()
        try {
            block()
            setTransactionSuccessful()
        } finally {
            endTransaction()
        }
    }
}