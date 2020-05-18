package com.wan.data.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wan.core.Resource
import com.wan.core.event.Event
import com.wan.core.extensions.copyTo
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource

/**
 * 收藏、取消收藏、获取收藏列表功能
 */
internal class CollectArticlesDelegate(
    private val articlesService: ArticlesService
) : AbsArticlesDelegate() {

    private val _collectStatus = MutableLiveData<Resource<CollectStatus>>()

    val collectStatus: LiveData<Event<Resource<CollectStatus>>> = _collectStatus.map {
        Event(it)
    }

    suspend fun collectArticle(articleId: Int, position: Int) {
        _collectStatus.value = Resource.loading()

        val resource = safeCall {
            val response = articlesService.collectArticle(articleId)
            response.toResource {
                Resource.success(it.data)
            }
        }

        _collectStatus.value = resource.copyTo { CollectStatus(true, position) }
    }

    suspend fun unCollectArticle(articleId: Int, position: Int) {
        _collectStatus.value = Resource.loading()

        val resource = safeCall {
            val response = articlesService.unCollectArticle(articleId)
            response.toResource {
                Resource.success(it.data)
            }
        }
        _collectStatus.value = resource.copyTo { CollectStatus(false, position) }
    }

    override suspend fun getArticles(page: Int): Resource<ArticlesModel> {
        return safeCall {
            val response = articlesService.getCollectArticlesByPage(page)
            convertArticlesModel(response)
        }
    }
}