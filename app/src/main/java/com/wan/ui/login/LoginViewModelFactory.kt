package com.wan.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.core.extensions.requireClassIsT
import com.wan.data.user.UserInjection

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, LoginViewModel::class.java) {
            LoginViewModel(
                UserInjection.provideLoginRepository()
            ) as T
        }
    }
}