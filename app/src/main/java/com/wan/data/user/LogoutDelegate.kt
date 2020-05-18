package com.wan.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wan.BaseInjection
import com.wan.bus.LogInOutBus
import com.wan.core.Resource
import com.wan.core.event.Event
import com.wan.core.extensions.safeCall
import com.wan.db.UserDao
import org.greenrobot.eventbus.EventBus

/**
 * 退出登录行为
 */
internal class LogoutDelegate(
    private val userDao: UserDao,
    private val userService: UserService
) {
    private val _logout = MutableLiveData<Resource<Nothing>>()

    val logout: LiveData<Event<Resource<Nothing>>> = _logout.map { Event(it) }

    suspend fun logout() {
        _logout.value = Resource.loading()

        val resource = safeCall {
            val response = userService.logout()
            Resource.success(response.data)
        }

        // 不管接口成功与否，也不管网络好不好，总之退出登录我们都希望它是成功的
        userDao.clearAllUsers()
        // 手动清除Cookie
        BaseInjection.provideRetrofitClient().clearCookie()
        // 始终返回成功
        _logout.value = Resource.success(resource.data)
        EventBus.getDefault().post(LogInOutBus(false))
    }
}