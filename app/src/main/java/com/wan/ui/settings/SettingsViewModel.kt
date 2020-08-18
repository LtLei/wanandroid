package com.wan.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.core.event.Event
import com.wan.core.network.MySerializableAny
import com.wan.data.user.LogoutRepository
import com.wan.data.user.User
import com.wan.data.user.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsViewModel(private val logoutRepository: LogoutRepository) : BaseViewModel() {
    private val _logout = MutableLiveData<Event<Resource<MySerializableAny>>>()
    val logout: LiveData<Event<Resource<MySerializableAny>>>
        get() = _logout

    private var logoutJob: Job? = null

    fun logout() {
        logoutJob = viewModelScope.launch {
            _logout.value = logoutRepository.logout()
        }
    }

    fun cancelLogout() {
        logoutJob?.cancel()
    }
}