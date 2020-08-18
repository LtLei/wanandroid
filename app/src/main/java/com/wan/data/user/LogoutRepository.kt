package com.wan.data.user

import com.wan.core.Resource
import com.wan.core.event.Event
import com.wan.core.network.MySerializableAny

interface LogoutRepository {
    suspend fun logout(): Event<Resource<MySerializableAny>>
}

/**
 * 退出登录行为
 */
class DefaultLogoutRepository(private val userRepository: UserRepository) : LogoutRepository {
    override suspend fun logout(): Event<Resource<MySerializableAny>> = userRepository.logout()
}