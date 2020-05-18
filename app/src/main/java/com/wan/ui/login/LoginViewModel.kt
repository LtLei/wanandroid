package com.wan.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.core.event.Event
import com.wan.data.user.LoginFormState
import com.wan.data.user.User
import com.wan.data.user.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val login: LiveData<Event<Resource<User>>> = userRepository.login

    val loginFormState: LiveData<Event<LoginFormState>> = userRepository.loginFormState

    private var loginJob: Job? = null

    fun login(username: String, password: String) {
        loginJob = viewModelScope.launch {
            userRepository.login(username, password)
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }

    fun onFormChange(username: String, password: String) {
        userRepository.onFormChange(username, password)
    }
}