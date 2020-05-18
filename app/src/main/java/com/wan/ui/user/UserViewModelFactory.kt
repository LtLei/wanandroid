package com.wan.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.core.extensions.requireClassIsT
import com.wan.data.user.UserInjection

class UserViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, UserViewModel::class.java) {
            UserViewModel(UserInjection.provideUserRepository()) as T
        }
    }
}