package com.wan.ui.classify

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.wan.core.Resource
import com.wan.core.base.BaseViewModel
import com.wan.data.classify.ClassifyModel
import com.wan.data.classify.ClassifyRepository
import kotlinx.coroutines.launch

class ClassifyViewModel(private val classifyRepository: ClassifyRepository) : BaseViewModel() {
    val classifies: LiveData<Resource<List<ClassifyModel>>> = classifyRepository.classifies

    fun getClassifies(forceUpdate: Boolean) {
        viewModelScope.launch {
            if (forceUpdate || classifies.value == null) {
                classifyRepository.getClassifies(forceUpdate)
            }
        }
    }
}