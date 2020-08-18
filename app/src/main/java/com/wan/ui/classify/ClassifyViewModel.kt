package com.wan.ui.classify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.data.classify.ClassifyModel
import com.wan.data.classify.ClassifyRepository

class ClassifyViewModel(private val classifyRepository: ClassifyRepository) : BaseViewModel() {
    private val _forceUpdate = MutableLiveData<Boolean>()
    val classifies: LiveData<Resource<List<ClassifyModel>>> =
        _forceUpdate.switchMap {
            liveData {
                emit(Resource.loading())
                emitSource(classifyRepository.getClassifies(it))
            }
        }

    fun getClassifies() {
        _forceUpdate.value = false
    }

    fun retry() {
        _forceUpdate.value = true
    }
}