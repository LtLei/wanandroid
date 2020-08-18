package com.wan.data.user

import com.wan.BaseInjection
import com.wan.db.DbInjection

object UserInjection {
    fun provideLoginRepository(): LoginRepository {
        return DefaultLoginRepository(provideUserRepository())
    }

    fun provideLogoutRepository(): LogoutRepository {
        return DefaultLogoutRepository(provideUserRepository())
    }

    fun provideUserRepository(): UserRepository {
        return DefaultUserRepository(
            DbInjection.provideUserDao(),
            provideUserService()
        )
    }

    private fun provideUserService(): UserService {
        return BaseInjection.provideApi(UserService::class.java)
    }
}