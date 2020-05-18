package com.wan.ui.articles

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wan.R
import com.wan.core.Resource
import com.wan.core.extensions.requireClassIsT
import com.wan.data.articles.ArticlesInjection
import com.wan.data.articles.ArticlesRepository
import com.wan.data.articles.ArticlesResult
import kotlinx.coroutines.launch

class CollectArticlesFragment : AbsArticlesFragment<CollectArticlesViewModel>() {
    override val viewModel: CollectArticlesViewModel by viewModels {
        CollectArticlesViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as? ArticlesActivity)?.supportActionBar?.title = getString(R.string.my_collect)
    }

    companion object {

        fun args(): Bundle {
            /* empty args. */
            return Bundle()
        }

        fun areArgsTheSame(oldBundle: Bundle, newBundle: Bundle): Boolean {
            /* ignore bundle difference. */
            return true
        }
    }
}

class CollectArticlesViewModel(
    private val articlesRepository: ArticlesRepository
) : AbsArticlesViewModel(articlesRepository) {
    override val articles: LiveData<Resource<ArticlesResult>>
        get() = articlesRepository.collectArticles

    override fun refresh() {
        viewModelScope.launch {
            articlesRepository.refreshCollectArticles()
        }
    }

    override fun loadMore() {
        viewModelScope.launch {
            articlesRepository.loadMoreCollectArticles()
        }
    }
}

class CollectArticlesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, CollectArticlesViewModel::class.java) {
            CollectArticlesViewModel(
                ArticlesInjection.provideArticlesRepository()
            ) as T
        }
    }
}