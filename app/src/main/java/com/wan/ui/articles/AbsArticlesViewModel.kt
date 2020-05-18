package com.wan.ui.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.core.event.Event
import com.wan.data.articles.ArticlesRepository
import com.wan.data.articles.ArticlesResult
import com.wan.data.articles.CollectStatus
import kotlinx.coroutines.launch

abstract class AbsArticlesViewModel(
    private val articlesRepository: ArticlesRepository
) : BaseViewModel() {
    abstract val articles: LiveData<Resource<ArticlesResult>>

    protected abstract fun refresh()

    abstract fun loadMore()

    fun refresh(forceUpdate: Boolean) {
        viewModelScope.launch {
            if (forceUpdate || articles.value == null) {
                refresh()
            }
        }
    }

    val collectStatus: LiveData<Event<Resource<CollectStatus>>> = articlesRepository.collectStatus

    fun collectArticle(articleId: Int, position: Int) {
        viewModelScope.launch {
            articlesRepository.collectArticle(articleId, position)
        }
    }

    fun unCollectArticle(articleId: Int, position: Int) {
        viewModelScope.launch {
            articlesRepository.unCollectArticle(articleId, position)
        }
    }
}
