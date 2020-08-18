package com.wan.data.classify

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.wan.MainCoroutineRule
import com.wan.core.Resource
import com.wan.core.network.ApiResponse
import com.wan.db.ClassifyDao
import com.wan.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ClassifyRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    private val classifyDao = mock(ClassifyDao::class.java)
    private val classifyService = mock(ClassifyService::class.java)

    private val classifyRepository = DefaultClassifyRepository(classifyDao, classifyService)

    @Test
    fun getClassifiesForceUpdate() = runBlocking {
        val models = listOf(
            ClassifyModel(classifyId = 0, name = "", courseId = 0, parentChapterId = 0)
        )
        val response = ApiResponse(data = models)
        `when`(classifyService.getClassifies()).thenReturn(response)

        val result = classifyRepository.getClassifies(true)

        val observer = mock<Observer<Resource<List<ClassifyModel>>>>()
        result.observeForever(observer)

        verify(classifyService).getClassifies()
        verify(classifyDao).insertClassifies(anyList())
        verifyNoMoreInteractions(classifyService)
        verifyNoMoreInteractions(classifyDao)
        verify(observer).onChanged(Resource.success(models))
    }

    @Test
    fun getClassifiesFromDao() = runBlocking {
        val models = listOf(
            ClassifyModel(classifyId = 0, name = "", courseId = 0, parentChapterId = 0)
        )
        `when`(classifyDao.getClassifies()).thenReturn(models)

        val result = classifyRepository.getClassifies(false)

        val observer = mock<Observer<Resource<List<ClassifyModel>>>>()
        result.observeForever(observer)

        verify(classifyDao).getClassifies()
        verifyNoMoreInteractions(classifyDao)
        verifyZeroInteractions(classifyService)
        verify(observer).onChanged(Resource.success(models))
    }

    @Test
    fun getClassifiesNoCache() = runBlocking {
        `when`(classifyDao.getClassifies()).thenReturn(emptyList())

        val models = listOf(
            ClassifyModel(classifyId = 0, name = "", courseId = 0, parentChapterId = 0)
        )
        val response = ApiResponse(data = models)
        `when`(classifyService.getClassifies()).thenReturn(response)

        val result = classifyRepository.getClassifies(false)

        val observer = mock<Observer<Resource<List<ClassifyModel>>>>()
        result.observeForever(observer)

        verify(classifyDao).getClassifies()
        verify(classifyDao).insertClassifies(anyList())
        verify(classifyService).getClassifies()
        verifyNoMoreInteractions(classifyDao)
        verifyNoMoreInteractions(classifyService)
        verify(observer).onChanged(Resource.success(models))
    }
}