package com.wan.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wan.core.Resource
import com.wan.core.extensions.requireClassIsT
import com.wan.data.articles.ArticlesInjection
import com.wan.data.articles.ArticlesRepository
import com.wan.data.articles.ArticlesResult
import com.wan.ui.articles.AbsArticlesFragment
import com.wan.ui.articles.AbsArticlesViewModel
import kotlinx.coroutines.launch

class HomeDetailFragment : AbsArticlesFragment<HomeViewModel>() {
    private val homeViewModelFactory by lazy {
        HomeViewModelFactory()
    }

    override val viewModel: HomeViewModel by viewModels {
        homeViewModelFactory
    }
}

class HomeViewModel(private val articlesRepository: ArticlesRepository) :
    AbsArticlesViewModel(articlesRepository) {
    override val articles: LiveData<Resource<ArticlesResult>>
        get() = articlesRepository.homeArticles

    override fun refresh() {
        viewModelScope.launch {
            articlesRepository.refreshHomeArticles()
        }
    }

    override fun loadMore() {
        viewModelScope.launch {
            articlesRepository.loadMoreHomeArticles()
        }
    }
}

class HomeViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, HomeViewModel::class.java) {
            HomeViewModel(
                ArticlesInjection.provideArticlesRepository()
            ) as T
        }
    }
}