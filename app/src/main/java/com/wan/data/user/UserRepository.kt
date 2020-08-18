package com.wan.data.user

import android.util.Patterns
import com.wan.R
import com.wan.bus.LogInOutBus
import com.wan.core.Resource
import com.wan.core.base.BaseRepository
import com.wan.core.event.Event
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource
import com.wan.core.network.MySerializableAny
import com.wan.db.UserDao
import org.greenrobot.eventbus.EventBus

interface UserRepository {
    suspend fun login(username: String, password: String): Event<Resource<User>>

    fun onFormChange(username: String, password: String): Event<LoginFormState>

    suspend fun logout(): Event<Resource<MySerializableAny>>
}

class DefaultUserRepository(private val userDao: UserDao, private val userService: UserService) :
    BaseRepository(), UserRepository {

    override suspend fun login(username: String, password: String): Event<Resource<User>> {
        val resource = safeCall {
            val response = userService.login(username, password)
            response.data?.let {
                userDao.insert(it)
            }
            response.toResource {
                if (it.data == null) {
                    Resource.empty()
                } else {
                    EventBus.getDefault().post(LogInOutBus(true))
                    Resource.success(it.data)
                }
            }
        }

        return Event(resource)
    }

    override fun onFormChange(username: String, password: String): Event<LoginFormState> {
        val state = if (!isUserNameValid(username)) {
            LoginFormState(userNameErr = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            LoginFormState(passwordErr = R.string.invalid_password)
        } else {
            LoginFormState(valid = true)
        }

        return Event(state)
    }

    override suspend fun logout(): Event<Resource<MySerializableAny>> {
        val resource = safeCall {
            val response = userService.logout()
            Resource.success(response.data)
        }

        // 不管接口成功与否，也不管网络好不好，总之退出登录我们都希望它是成功的
        userDao.clearAllUsers()
        EventBus.getDefault().post(LogInOutBus(false))
        return Event(Resource.success(resource.data))
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 6..20
    }
}