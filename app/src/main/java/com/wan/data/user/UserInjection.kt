package com.wan.data.user

import com.wan.BaseInjection
import com.wan.db.DbInjection

object UserInjection {
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl.getInstance(
            DbInjection.provideUserDao(),
            provideUserService()
        )
    }

    private fun provideUserService(): UserService {
        return BaseInjection.provideApi(UserService::class.java)
    }
}