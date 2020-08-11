package com.wan.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.core.event.Event
import com.wan.core.network.MySerializableAny
import com.wan.data.user.User
import com.wan.data.user.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {
    val logout: LiveData<Event<Resource<MySerializableAny>>>
        get() = userRepository.logout
    val user: LiveData<User>
        get() = userRepository.getUserLiveData()

    private var logoutJob: Job? = null

    fun logout() {
        logoutJob = viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun cancelLogout() {
        logoutJob?.cancel()
    }
}