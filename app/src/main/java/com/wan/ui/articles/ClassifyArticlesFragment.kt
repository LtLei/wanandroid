package com.wan.ui.articles

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.core.Resource
import com.wan.core.extensions.requireClassIsT
import com.wan.data.articles.ArticlesInjection
import com.wan.data.articles.ArticlesRepository
import com.wan.data.articles.ArticlesResult
import kotlin.properties.Delegates

class ClassifyArticlesFragment : AbsArticlesFragment<ClassifyArticlesViewModel>() {
    override val viewModel: ClassifyArticlesViewModel by viewModels {
        ClassifyArticlesViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.classifyId =
            requireNotNull(arguments?.getInt(ARG_CLASSIFY_ID), { "ARG_CLASSIFY_ID is null." })

        val name = requireNotNull(
            arguments?.getString(ARG_CLASSIFY_NAME),
            { "ARG_CLASSIFY_NAME is null." })
        (activity as? ArticlesActivity)?.supportActionBar?.title = name
    }

    companion object {
        private const val ARG_CLASSIFY_ID = "arg_classify_id"
        private const val ARG_CLASSIFY_NAME = "arg_classify_name"

        fun args(classifyId: Int, classifyName: String): Bundle {
            return Bundle().apply {
                putInt(ARG_CLASSIFY_ID, classifyId)
                putString(ARG_CLASSIFY_NAME, classifyName)
            }
        }

        fun areArgsTheSame(oldBundle: Bundle, newBundle: Bundle): Boolean {
            val oldId = oldBundle.getInt(ARG_CLASSIFY_ID)
            val newId = newBundle.getInt(ARG_CLASSIFY_ID)
            return oldId == newId
        }
    }
}

class ClassifyArticlesViewModel(
    private val articlesRepository: ArticlesRepository
) : AbsArticlesViewModel(articlesRepository) {
    var classifyId by Delegates.notNull<Int>()

    override suspend fun refresh(): Resource<ArticlesResult> {
        return articlesRepository.refreshClassifyArticles(classifyId)
    }

    override suspend fun _loadMore(): Resource<ArticlesResult> {
        return articlesRepository.loadMoreClassifyArticles(classifyId)
    }
}

class ClassifyArticlesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, ClassifyArticlesViewModel::class.java) {
            ClassifyArticlesViewModel(
                ArticlesInjection.provideArticlesRepository()
            ) as T
        }
    }
}