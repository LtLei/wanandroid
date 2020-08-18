package com.wan.data.user

import com.wan.core.Resource
import com.wan.core.event.Event

interface LoginRepository {
    suspend fun login(username: String, password: String): Event<Resource<User>>

    fun onFormChange(username: String, password: String): Event<LoginFormState>
}

class DefaultLoginRepository(private val userRepository: UserRepository) : LoginRepository {
    override suspend fun login(username: String, password: String): Event<Resource<User>> =
        userRepository.login(username, password)

    override fun onFormChange(username: String, password: String): Event<LoginFormState> =
        userRepository.onFormChange(username, password)
}