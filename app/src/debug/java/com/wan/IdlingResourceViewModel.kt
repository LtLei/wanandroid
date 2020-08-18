package com.wan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wan.core.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@OpenForTesting
class IdlingResourceViewModel : BaseViewModel() {
    private val _fakeData = MutableLiveData<Int>()
    val fakeData: LiveData<Int>
        get() = _fakeData

    fun fetchFakeData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000)
                withContext(Dispatchers.Main) {
                    _fakeData.postValue(R.string.text_login_success)
                }
            }
        }
    }
}