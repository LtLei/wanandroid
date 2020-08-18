package com.wan.db

import com.wan.BaseInjection

object DbInjection {
    fun provideClassifyDao(): ClassifyDao {
        return provideDb().classifyDao()
    }

    fun provideUserDao(): UserDao {
        return provideDb().userDao()
    }

    private fun provideDb(): WanAndroidDb {
        return WanAndroidDb.getInstance(
            BaseInjection.provideApp()
        )
    }
}