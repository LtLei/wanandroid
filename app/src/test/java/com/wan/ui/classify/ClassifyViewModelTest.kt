package com.wan.ui.classify

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wan.MainCoroutineRule
import com.wan.core.Resource
import com.wan.data.classify.ClassifyModel
import com.wan.data.classify.ClassifyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ClassifyViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    private val classifyRepository = mock(ClassifyRepository::class.java)
    private val classifyViewModel = ClassifyViewModel(classifyRepository)

    @Test
    fun retry() {
        val models = emptyList<ClassifyModel>()
        val res = MutableLiveData(Resource.success(models))
        `when`(classifyRepository.getClassifies(true)).thenReturn(res)

        val observer = com.wan.mock<Observer<Resource<List<ClassifyModel>>>>()
        classifyViewModel.classifies.observeForever(observer)
        classifyViewModel.retry()

        verify(observer, times(1)).onChanged(Resource.loading())
        verify(observer).onChanged(Resource.success(models))
    }
}