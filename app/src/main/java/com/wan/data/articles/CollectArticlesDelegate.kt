package com.wan.data.articles

import com.wan.core.Resource
import com.wan.core.extensions.safeCall

/**
 * 收藏、取消收藏、获取收藏列表功能
 */
internal class CollectArticlesDelegate(
    private val articlesService: ArticlesService
) : AbsArticlesDelegate() {

    override suspend fun getArticles(page: Int): Resource<ArticlesModel> {
        return safeCall {
            val response = articlesService.getCollectArticlesByPage(page)
            convertArticlesModel(response)
        }
    }
}