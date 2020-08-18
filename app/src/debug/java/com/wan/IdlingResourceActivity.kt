package com.wan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.IdlingResource
import com.wan.core.base.BaseActivity
import kotlinx.android.synthetic.debug.idling_resource_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class IdlingResourceActivity : BaseActivity() {
    private val viewModelFactory: ViewModelProvider.Factory =
        IdlingResourceInjection.provideViewModelFactory()
    private val viewModel: IdlingResourceViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.idling_resource_activity)
        btn_login.setOnClickListener {
//            lifecycleScope.launch {
//                fetchFakeData {
//                    tv_login_success.text = it
//                }
//            }

            viewModel.fetchFakeData()
        }

        viewModel.fakeData.observe(this, {
            tv_login_success.text = getString(it)
        })
    }

    private suspend fun fetchFakeData(callback: (text: String) -> Unit) {
        withContext(Dispatchers.IO) {
            EspressoIdlingResource.increment()
            delay(3000)
            withContext(Dispatchers.Main) {
                EspressoIdlingResource.decrement()
                callback.invoke(getString(R.string.text_login_success))
            }
        }
    }

    @VisibleForTesting
    fun getIdlingResource(): IdlingResource {
        return EspressoIdlingResource.countingIdlingResource
    }
}