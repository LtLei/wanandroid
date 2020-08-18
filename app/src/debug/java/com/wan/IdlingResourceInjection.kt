package com.wan

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider

object IdlingResourceInjection {
    @VisibleForTesting
    var viewModel = IdlingResourceViewModel()

    private fun provideViewModel(): IdlingResourceViewModel {
        return viewModel
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return IdlingResourceViewModelFactory(provideViewModel())
    }
}