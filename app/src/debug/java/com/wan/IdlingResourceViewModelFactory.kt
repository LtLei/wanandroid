package com.wan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wan.core.extensions.requireClassIsT

@Suppress("UNCHECKED_CAST")
class IdlingResourceViewModelFactory(private val viewModel: IdlingResourceViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return requireClassIsT(modelClass, IdlingResourceViewModel::class.java) {
            viewModel as T
        }
    }
}