package com.wan.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.wan.R
import com.wan.core.State
import com.wan.core.base.BaseActivity
import com.wan.core.base.LoadingDialogFragment
import com.wan.core.event.EventObserver
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : BaseActivity() {
    private val viewModelFactory by lazy { SettingsViewModelFactory() }
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        displayHomeAsUp()

        viewModel.user.observe(this, Observer {
            btn_logout.isEnabled = it != null
            if (it != null) {
                btn_logout.setOnClickListener {
                    viewModel.logout()
                }
            }
        })

        viewModel.logout.observe(this, EventObserver {
            if (it.state == State.LOADING) {
                LoadingDialogFragment.show(supportFragmentManager, true,
                    DialogInterface.OnCancelListener { viewModel.cancelLogout() })
            } else {
                LoadingDialogFragment.hide(supportFragmentManager)
                if (it.state == State.SUCCESS) {
                    Toast.makeText(this, getString(R.string.logout_successful), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.logout_failed_retry),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}