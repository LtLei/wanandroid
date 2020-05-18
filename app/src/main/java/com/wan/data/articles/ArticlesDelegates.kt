package com.wan.data.articles

import com.wan.core.Resource
import com.wan.core.coroutines.ControlledRunner
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.properties.Delegates

internal class HomeArticlesDelegate(private val articlesService: ArticlesService) :
    AbsArticlesDelegate() {
    override suspend fun getArticles(page: Int): Resource<ArticlesModel> {
        return if (page == 0) {
            getTopAndFirstPageArticles()
        } else {
            getHomeArticlesByPage(page)
        }
    }

    private suspend fun getTopAndFirstPageArticles(): Resource<ArticlesModel> {
        return coroutineScope {
            val t1 = async { getTopArticles() }
            val t2 = async { getHomeArticlesByPage(0) }

            val res1 = t1.await()
            val res2 = t2.await()
            res1.data?.let { tops ->
                tops.forEach { it.top = true }

                res2.data?.articles?.addAll(0, tops)
            }

            res2
        }
    }

    private suspend fun getTopArticles(): Resource<List<Article>> {
        return safeCall {
            val response = articlesService.getTopArticles()
            return response.toResource {
                if (it.data.isNullOrEmpty()) {
                    Resource.empty()
                } else {
                    Resource.success(it.data)
                }
            }
        }
    }

    private suspend fun getHomeArticlesByPage(page: Int): Resource<ArticlesModel> {
        return safeCall {
            val response = articlesService.getHomeArticlesByPage(page)
            return convertArticlesModel(response)
        }
    }
}

internal class AuthorArticlesDelegate(private val articlesService: ArticlesService) :
    AbsArticlesDelegate() {
    var author: String by Delegates.notNull()

    override suspend fun getArticles(page: Int): Resource<ArticlesModel> {
        return safeCall {
            val response = articlesService.getArticlesByAuthor(page, author)
            return convertArticlesModel(response)
        }
    }
}

internal class ClassifyArticlesDelegate(private val articlesService: ArticlesService) :
    AbsArticlesDelegate() {
    var classifyId: Int by Delegates.notNull()

    override suspend fun getArticles(page: Int): Resource<ArticlesModel> {
        return safeCall {
            val response = articlesService.getArticlesByClassify(page, classifyId)
            return convertArticlesModel(response)
        }
    }
}

internal class KeywordArticlesDelegate(private val articlesService: ArticlesService) :
    AbsArticlesDelegate() {
    var keyword: String by Delegates.notNull()

    private val searchRunner by lazy { ControlledRunner<Resource<ArticlesModel>>() }

    override suspend fun getArticles(page: Int): Resource<ArticlesModel> {
        return searchRunner.cancelPreviousThenRun {
            safeCall {
                val response = articlesService.getArticlesByAuthor(page, keyword)
                convertArticlesModel(response)
            }
        }
    }
}