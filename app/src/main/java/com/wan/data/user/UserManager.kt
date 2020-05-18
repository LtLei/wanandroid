package com.wan.data.user

object UserManager {
    private val userRepository by lazy { UserInjection.provideUserRepository() }

    fun isLogin(): Boolean {
        return userRepository.isLogin()
    }

    fun getUser(): User? = userRepository.getUser()
}