package com.wan.core

import com.wan.core.constant.SUCCESS_CODE
import kotlinx.serialization.Serializable



/**
 * 从数据加载来看，只有 加载中，成功，失败，没有数据 几种情况。
 *
 * 将[com.wan.core.network.ApiResponse] 转换成有状态的 [Resource] 可以更好地被 UI 使用。
 */
@Serializable
data class Resource<out T>(val state: State, val data: T?, val code: Int?, val msg: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(State.SUCCESS, data, SUCCESS_CODE, null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(State.EMPTY, null, null, null)
        }

        fun <T> error(code: Int, msg: String): Resource<T> {
            return Resource(State.ERROR, null, code, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(State.LOADING, null, null, null)
        }
    }
}
