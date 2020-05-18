package com.wan.ui.search

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.wan.core.Resource
import com.wan.core.extensions.requireClassIsT
import com.wan.data.articles.ArticlesInjection
import com.wan.data.articles.ArticlesRepository
import com.wan.data.articles.ArticlesResult
import com.wan.ui.articles.AbsArticlesFragment
import com.wan.ui.articles.AbsArticlesViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SearchFragment : AbsArticlesFragment<SearchViewModel>() {
    override val viewModel: SearchViewModel by viewModels { SearchViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keyword = requireNotNull(arguments?.getString(ARG_KEYWORD), { "ARG_KEYWORD is null." })
        viewModel.keyword = keyword
    }

    fun startSearch(keyword: String) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            if (viewModel.keyword != keyword) {
                viewModel.keyword = keyword
                viewModel.refresh(true)
            }
        }
    }

    companion object {
        private const val ARG_KEYWORD = "arg_keyword"

        fun startSearch(fragmentManager: FragmentManager, containerId: Int, keyword: String) {
            val fragment = fragmentManager.findFragmentById(containerId)
            if (fragment != null) {
                (fragment as SearchFragment).startSearch(keyword)
            } else {
                fragmentManager.beginTransaction()
                    .add(containerId, SearchFragment().apply {
                        arguments = Bundle().apply {
                            putString(ARG_KEYWORD, keyword)
                        }
                    })
                    .commit()
            }
        }
    }
}

class SearchViewModel(
    private val articlesRepository: ArticlesRepository
) : AbsArticlesViewModel(articlesRepository) {
    var keyword: String by Delegates.notNull()

    override val articles: LiveData<Resource<ArticlesResult>>
        get() = articlesRepository.keywordArticles

    override fun refresh() {
        viewModelScope.launch {
            articlesRepository.refreshKeywordArticles(keyword)
        }
    }

    override fun loadMore() {
        viewModelScope.launch {
            articlesRepository.loadMoreKeywordArticles(keyword)
        }
    }
}

class SearchViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return requireClassIsT(modelClass, SearchViewModel::class.java) {
            SearchViewModel(
                ArticlesInjection.provideArticlesRepository()
            ) as T
        }
    }
}