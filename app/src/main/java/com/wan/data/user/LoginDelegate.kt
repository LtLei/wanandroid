package com.wan.data.user

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wan.R
import com.wan.bus.LogInOutBus
import com.wan.core.Resource
import com.wan.core.event.Event
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource
import com.wan.db.UserDao
import org.greenrobot.eventbus.EventBus

internal class LoginDelegate(
    private val userDao: UserDao,
    private val userService: UserService
) {
    private val _login = MutableLiveData<Resource<User>>()

    val login: LiveData<Event<Resource<User>>> = _login.map { Event(it) }

    private val _loginFormState = MutableLiveData<LoginFormState>()

    val loginFormState: LiveData<Event<LoginFormState>> = _loginFormState.map { Event(it) }

    suspend fun login(username: String, password: String) {
        _login.value = Resource.loading()

        _login.value = safeCall {
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
    }

    fun onFormChange(username: String, password: String) {
        _loginFormState.value = if (!isUserNameValid(username)) {
            LoginFormState(userNameErr = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            LoginFormState(passwordErr = R.string.invalid_password)
        } else {
            LoginFormState(valid = true)
        }
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