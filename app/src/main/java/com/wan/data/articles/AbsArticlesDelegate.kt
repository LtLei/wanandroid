package com.wan.data.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wan.core.Resource
import com.wan.core.State
import com.wan.core.extensions.copyTo
import com.wan.core.extensions.toResource
import com.wan.core.network.ApiResponse

internal abstract class AbsArticlesDelegate {
    abstract suspend fun getArticles(page: Int): Resource<ArticlesModel>

    private var _page: Int = 0
    private val _articles = arrayListOf<Article>()

    private val _articlesResult = MutableLiveData<Resource<ArticlesResult>>()

    val articlesResult: LiveData<Resource<ArticlesResult>> = _articlesResult

    suspend fun refresh() {
        _page = 0
        _articles.clear()
        getArticlesByPage(true)
    }

    suspend fun loadMore() {
        getArticlesByPage(false)
    }

    private suspend fun getArticlesByPage(refresh: Boolean) {
        if (refresh) {
            _articlesResult.value = Resource.loading()
        }
        val resource = getArticles(_page)

        val hasMore = resource.data?.over?.not() ?: false
        resource.data?.articles?.let {
            _articles.addAll(it)
        }
        if (resource.state == State.SUCCESS) {
            ++_page
        }
        _articlesResult.value =
            resource.copyTo { ArticlesResult(refresh, hasMore, _articles.toMutableList()) }
    }

    protected fun convertArticlesModel(response: ApiResponse<ArticlesModel>): Resource<ArticlesModel> {
        return response.toResource {
            if (it.data == null || it.data?.articles.isNullOrEmpty()) {
                Resource.empty()
            } else {
                Resource.success(it.data)
            }
        }
    }
}