package com.wan.ui.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _articles = MutableLiveData<Resource<ArticlesResult>>()
    val articles: LiveData<Resource<ArticlesResult>>
        get() = _articles

    protected abstract suspend fun refresh(): Resource<ArticlesResult>

    abstract suspend fun _loadMore(): Resource<ArticlesResult>

    fun refresh(forceUpdate: Boolean) {
        viewModelScope.launch {
            if (forceUpdate || articles.value == null) {
                _articles.value = Resource.loading()
                _articles.value = refresh()
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            _articles.value = _loadMore()
        }
    }

    private val _collectStatus = MutableLiveData<Event<Resource<CollectStatus>>>()

    val collectStatus: LiveData<Event<Resource<CollectStatus>>>
        get() = _collectStatus

    fun collectArticle(articleId: Int, position: Int) {
        viewModelScope.launch {
            _collectStatus.value = Event(Resource.loading())
            _collectStatus.value = articlesRepository.collectArticle(articleId, position)
        }
    }

    fun unCollectArticle(articleId: Int, position: Int) {
        viewModelScope.launch {
            _collectStatus.value = Event(Resource.loading())
            _collectStatus.value = articlesRepository.unCollectArticle(articleId, position)
        }
    }
}
