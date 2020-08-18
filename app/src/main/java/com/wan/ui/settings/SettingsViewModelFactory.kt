package com.wan.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.core.extensions.requireClassIsT
import com.wan.data.user.UserInjection

class SettingsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, SettingsViewModel::class.java) {
            return SettingsViewModel(UserInjection.provideLogoutRepository()) as T
        }
    }
}