package com.wan.ui.classify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.core.extensions.requireClassIsT
import com.wan.data.classify.ClassifyInjection

class ClassifyViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return requireClassIsT(modelClass, ClassifyViewModel::class.java) {
            ClassifyViewModel(ClassifyInjection.provideClassifyRepository()) as T
        }
    }
}