package com.wan.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wan.OpenForTesting
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.core.event.Event
import com.wan.data.user.LoginFormState
import com.wan.data.user.LoginRepository
import com.wan.data.user.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OpenForTesting
class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {
    private val _login = MutableLiveData<Event<Resource<User>>>()
    val login: LiveData<Event<Resource<User>>>
        get() = _login

    private val _loginFormState = MutableLiveData<Event<LoginFormState>>()
    val loginFormState: LiveData<Event<LoginFormState>>
        get() = _loginFormState

    private var loginJob: Job? = null

    fun login(username: String, password: String) {
        loginJob = viewModelScope.launch {
            _login.value = Event(Resource.loading())

            _login.value = loginRepository.login(username, password)
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }

    fun onFormChange(username: String, password: String) {
        viewModelScope.launch {
            _loginFormState.value = loginRepository.onFormChange(username, password)
        }
    }
}