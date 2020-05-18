package com.wan.data.articles

import androidx.lifecycle.LiveData
import com.wan.core.Resource
import com.wan.core.base.BaseRepository
import com.wan.core.event.Event

/**
 * 加载文章列表
 */
interface ArticlesRepository {
    val collectStatus: LiveData<Event<Resource<CollectStatus>>>

    /* @param position 文章在列表中的位置，以方便更新UI */
    suspend fun collectArticle(articleId: Int, position: Int)

    /* @param position 文章在列表中的位置，以方便更新UI */
    suspend fun unCollectArticle(articleId: Int, position: Int)

    val collectArticles: LiveData<Resource<ArticlesResult>>
    suspend fun refreshCollectArticles()
    suspend fun loadMoreCollectArticles()

    val homeArticles: LiveData<Resource<ArticlesResult>>
    suspend fun refreshHomeArticles()
    suspend fun loadMoreHomeArticles()

    val authorArticles: LiveData<Resource<ArticlesResult>>
    suspend fun refreshAuthorArticles(author: String)
    suspend fun loadMoreAuthorArticles(author: String)

    val classifyArticles: LiveData<Resource<ArticlesResult>>
    suspend fun refreshClassifyArticles(classifyId: Int)
    suspend fun loadMoreClassifyArticles(classifyId: Int)

    val keywordArticles: LiveData<Resource<ArticlesResult>>
    suspend fun refreshKeywordArticles(keyword: String)
    suspend fun loadMoreKeywordArticles(keyword: String)
}

class ArticlesRepositoryImpl(private val articlesService: ArticlesService) : BaseRepository(),
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

    override val collectStatus: LiveData<Event<Resource<CollectStatus>>>
        get() = collectArticlesDelegate.collectStatus

    override suspend fun collectArticle(articleId: Int, position: Int) {
        collectArticlesDelegate.collectArticle(articleId, position)
    }

    override suspend fun unCollectArticle(articleId: Int, position: Int) {
        collectArticlesDelegate.unCollectArticle(articleId, position)
    }

    override val collectArticles: LiveData<Resource<ArticlesResult>>
        get() = collectArticlesDelegate.articlesResult

    override suspend fun refreshCollectArticles() {
        collectArticlesDelegate.refresh()
    }

    override suspend fun loadMoreCollectArticles() {
        collectArticlesDelegate.loadMore()
    }

    override val homeArticles: LiveData<Resource<ArticlesResult>>
        get() = homeArticlesDelegate.articlesResult

    override suspend fun refreshHomeArticles() {
        homeArticlesDelegate.refresh()
    }

    override suspend fun loadMoreHomeArticles() {
        homeArticlesDelegate.loadMore()
    }

    override val authorArticles: LiveData<Resource<ArticlesResult>>
        get() = authorArticlesDelegate.articlesResult

    override suspend fun refreshAuthorArticles(author: String) {
        authorArticlesDelegate.author = author
        authorArticlesDelegate.refresh()
    }

    override suspend fun loadMoreAuthorArticles(author: String) {
        authorArticlesDelegate.author = author
        authorArticlesDelegate.loadMore()
    }

    override val classifyArticles: LiveData<Resource<ArticlesResult>>
        get() = classifyArticlesDelegate.articlesResult

    override suspend fun refreshClassifyArticles(classifyId: Int) {
        classifyArticlesDelegate.classifyId = classifyId
        classifyArticlesDelegate.refresh()
    }

    override suspend fun loadMoreClassifyArticles(classifyId: Int) {
        classifyArticlesDelegate.classifyId = classifyId
        classifyArticlesDelegate.loadMore()
    }

    override val keywordArticles: LiveData<Resource<ArticlesResult>>
        get() = keywordArticlesDelegate.articlesResult

    override suspend fun refreshKeywordArticles(keyword: String) {
        keywordArticlesDelegate.keyword = keyword
        keywordArticlesDelegate.refresh()
    }

    override suspend fun loadMoreKeywordArticles(keyword: String) {
        keywordArticlesDelegate.keyword = keyword
        keywordArticlesDelegate.loadMore()
    }
}