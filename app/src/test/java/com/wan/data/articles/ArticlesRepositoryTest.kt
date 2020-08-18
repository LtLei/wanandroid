package com.wan.data.articles

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wan.MainCoroutineRule
import com.wan.core.network.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ArticlesRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    private val articlesService = Mockito.mock(ArticlesService::class.java)
    private val articlesRepository = DefaultArticlesRepository(articlesService)

    @Test
    fun refreshAndLoadMore() = runBlocking {
        val refreshArticles = createArticles(true)
        val refreshResponse = ApiResponse(
                data = ArticlesModel(0, 2, false, refreshArticles)
        )

        val moreArticles = createArticles(false)
        val moreResponse = ApiResponse(
                data = ArticlesModel(1, 2, true, moreArticles)
        )

        `when`(articlesService.getCollectArticlesByPage(0)).thenReturn(refreshResponse)
        `when`(articlesService.getCollectArticlesByPage(1)).thenReturn(moreResponse)

        val refresh = articlesRepository.refreshCollectArticles()
        assertEquals(refresh.data, ArticlesResult(true, true, refreshArticles))

        val loadMore = articlesRepository.loadMoreCollectArticles()
        val allArticles = mutableListOf<Article>()
        allArticles.addAll(refreshArticles)
        allArticles.addAll(moreArticles)
        assertEquals(loadMore.data, ArticlesResult(false, false, allArticles))
    }

    private fun createArticles(refresh: Boolean): MutableList<Article> {
        return if (refresh) {
            mutableListOf(
                    createArticle(),
                    createArticle(),
                    createArticle()
            )
        } else {
            mutableListOf(
                    createArticle(),
                    createArticle()
            )
        }
    }

    private fun createArticle(): Article {
        return Article(
                id = 0,
                title = "",
                desc = null,
                link = "",
                chapterId = 0,
                chapterName = null,
                superChapterId = 0,
                superChapterName = null,
                userId = 0,
                author = null,
                niceDate = null,
                shareUser = null,
                niceShareDate = null,
                collect = false,
                fresh = false,
                zan = 0,
                tags = listOf(),
                top = false
        )
    }
}