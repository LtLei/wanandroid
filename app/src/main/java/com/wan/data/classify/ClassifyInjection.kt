package com.wan.data.classify

import com.wan.BaseInjection
import com.wan.db.DbInjection

object ClassifyInjection {
    fun provideClassifyRepository(): ClassifyRepository {
        return ClassifyRepositoryImpl(
            DbInjection.provideDb(),
            DbInjection.provideClassifyDao(),
            provideClassifyService()
        )
    }

    private fun provideClassifyService(): ClassifyService {
        return BaseInjection.provideApi(ClassifyService::class.java)
    }

}