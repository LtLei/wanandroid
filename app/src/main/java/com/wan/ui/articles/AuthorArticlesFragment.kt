package com.wan.ui.articles

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.R
import com.wan.core.Resource
import com.wan.core.extensions.requireClassIsT
import com.wan.data.articles.ArticlesInjection
import com.wan.data.articles.ArticlesRepository
import com.wan.data.articles.ArticlesResult
import kotlin.properties.Delegates

class AuthorArticlesFragment : AbsArticlesFragment<AuthorArticlesViewModel>() {
    override val viewModel: AuthorArticlesViewModel by viewModels {
        AuthorArticlesViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val author = requireNotNull(arguments?.getString(ARG_AUTHOR), { "ARG_AUTHOR is null." })
        viewModel.author = author

        (activity as? ArticlesActivity)?.supportActionBar?.title =
            getString(R.string.author_articles, author)
    }

    companion object {
        private const val ARG_AUTHOR = "arg_author"

        fun args(author: String): Bundle {
            return Bundle().apply {
                putString(ARG_AUTHOR, author)
            }
        }

        fun areArgsTheSame(oldBundle: Bundle, newBundle: Bundle): Boolean {
            val oldAuthor = oldBundle.getString(ARG_AUTHOR)
            val newAuthor = newBundle.getString(ARG_AUTHOR)
            return oldAuthor == newAuthor
        }
    }
}

class AuthorArticlesViewModel(
    private val articlesRepository: ArticlesRepository
) : AbsArticlesViewModel(articlesRepository) {
    var author: String by Delegates.notNull()


    override suspend fun refresh(): Resource<ArticlesResult> {
        return articlesRepository.refreshAuthorArticles(author)
    }

    override suspend fun _loadMore(): Resource<ArticlesResult> {
        return articlesRepository.loadMoreAuthorArticles(author)
    }
}

class AuthorArticlesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, AuthorArticlesViewModel::class.java) {
            AuthorArticlesViewModel(
                ArticlesInjection.provideArticlesRepository()
            ) as T
        }
    }
}