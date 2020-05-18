package com.wan.db

import com.wan.BaseInjection

object DbInjection {
    fun provideDb(): WanAndroidDb {
        return WanAndroidDb.getInstance(
            BaseInjection.provideApp()
        )
    }

    fun provideClassifyDao(): ClassifyDao {
        return provideDb().classifyDao()
    }

    fun provideUserDao(): UserDao {
        return provideDb().userDao()
    }
}