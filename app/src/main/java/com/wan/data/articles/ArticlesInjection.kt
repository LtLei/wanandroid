package com.wan.data.articles

import com.wan.BaseInjection

object ArticlesInjection {
    fun provideArticlesRepository(): ArticlesRepository {
        return ArticlesRepositoryImpl(provideArticlesService())
    }

    private fun provideArticlesService(): ArticlesService {
        return BaseInjection.provideApi(ArticlesService::class.java)
    }
}