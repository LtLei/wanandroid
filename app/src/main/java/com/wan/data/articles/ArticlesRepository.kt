package com.wan.data.articles

import com.wan.core.Resource
import com.wan.core.base.BaseRepository
import com.wan.core.event.Event
import com.wan.core.extensions.copyTo
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource

/**
 * 加载文章列表
 */
interface ArticlesRepository {
    /* @param position 文章在列表中的位置，以方便更新UI */
    suspend fun collectArticle(articleId: Int, position: Int): Event<Resource<CollectStatus>>

    /* @param position 文章在列表中的位置，以方便更新UI */
    suspend fun unCollectArticle(articleId: Int, position: Int): Event<Resource<CollectStatus>>

    suspend fun refreshCollectArticles(): Resource<ArticlesResult>
    suspend fun loadMoreCollectArticles(): Resource<ArticlesResult>

    suspend fun refreshHomeArticles(): Resource<ArticlesResult>
    suspend fun loadMoreHomeArticles(): Resource<ArticlesResult>

    suspend fun refreshAuthorArticles(author: String): Resource<ArticlesResult>
    suspend fun loadMoreAuthorArticles(author: String): Resource<ArticlesResult>

    suspend fun refreshClassifyArticles(classifyId: Int): Resource<ArticlesResult>
    suspend fun loadMoreClassifyArticles(classifyId: Int): Resource<ArticlesResult>

    suspend fun refreshKeywordArticles(keyword: String): Resource<ArticlesResult>
    suspend fun loadMoreKeywordArticles(keyword: String): Resource<ArticlesResult>
}

class DefaultArticlesRepository(private val articlesService: ArticlesService) : BaseRepository(),
    ArticlesRepository {

    private val collectArticlesDelegate by lazy {
        CollectArticlesDelegate(articlesService)
    }

    private val homeArticlesDelegate by lazy {
        HomeArticlesDelegate(articlesService)
    }

    private val authorArticlesDelegate by lazy {
        AuthorArticlesDelegate(articlesService)
    }

    private val classifyArticlesDelegate by lazy {
        ClassifyArticlesDelegate(articlesService)
    }

    private val keywordArticlesDelegate by lazy {
        KeywordArticlesDelegate(articlesService)
    }

    override suspend fun collectArticle(
        articleId: Int,
        position: Int
    ): Event<Resource<CollectStatus>> {
        val resource = safeCall {
            val response = articlesService.collectArticle(articleId)
            response.toResource {
                Resource.success(it.data)
            }
        }

        return Event(resource.copyTo { CollectStatus(true, position) })
    }

    override suspend fun unCollectArticle(
        articleId: Int,
        position: Int
    ): Event<Resource<CollectStatus>> {
        val resource = safeCall {
            val response = articlesService.unCollectArticle(articleId)
            response.toResource {
                Resource.success(it.data)
            }
        }
        return Event(resource.copyTo { CollectStatus(false, position) })
    }


    override suspend fun refreshCollectArticles(): Resource<ArticlesResult> {
        return collectArticlesDelegate.refresh()
    }

    override suspend fun loadMoreCollectArticles(): Resource<ArticlesResult> {
        return collectArticlesDelegate.loadMore()
    }

    override suspend fun refreshHomeArticles(): Resource<ArticlesResult> {
        return homeArticlesDelegate.refresh()
    }

    override suspend fun loadMoreHomeArticles(): Resource<ArticlesResult> {
        return homeArticlesDelegate.loadMore()
    }

    override suspend fun refreshAuthorArticles(author: String): Resource<ArticlesResult> {
        authorArticlesDelegate.author = author
        return authorArticlesDelegate.refresh()
    }

    override suspend fun loadMoreAuthorArticles(author: String): Resource<ArticlesResult> {
        authorArticlesDelegate.author = author
        return authorArticlesDelegate.loadMore()
    }

    override suspend fun refreshClassifyArticles(classifyId: Int): Resource<ArticlesResult> {
        classifyArticlesDelegate.classifyId = classifyId
        return classifyArticlesDelegate.refresh()
    }

    override suspend fun loadMoreClassifyArticles(classifyId: Int): Resource<ArticlesResult> {
        classifyArticlesDelegate.classifyId = classifyId
        return classifyArticlesDelegate.loadMore()
    }


    override suspend fun refreshKeywordArticles(keyword: String): Resource<ArticlesResult> {
        keywordArticlesDelegate.keyword = keyword
        return keywordArticlesDelegate.refresh()
    }

    override suspend fun loadMoreKeywordArticles(keyword: String): Resource<ArticlesResult> {
        keywordArticlesDelegate.keyword = keyword
        return keywordArticlesDelegate.loadMore()
    }
}