package com.wan.ui.login

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.wan.R
import com.wan.core.State
import com.wan.core.base.BaseActivity
import com.wan.core.base.LoadingDialogFragment
import com.wan.core.event.EventObserver
import com.wan.core.util.HideKeyBoard
import kotlinx.android.synthetic.main.login_activity.*

/**
 * 登录页
 *
 * 当你需要根据用户登录状态来刷新页面时，[com.wan.data.user.UserRepository.getUserLiveData]会在登录与登出后自动更新，所以只需要执行 [startActivity] 方法。
 *
 * 当你进行某个操作后需要触发登录，并希望登录成功后继续之前的操作时，请使用 [startActivityForResult] 方法。
 */
class LoginActivity : BaseActivity() {
    private val viewModelFactory by lazy { LoginViewModelFactory() }
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_activity)

        btn_close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        et_username.doAfterTextChanged {
            viewModel.onFormChange(
                et_username.text.toString(),
                et_password.text.toString()
            )
        }

        et_password.doAfterTextChanged {
            viewModel.onFormChange(
                et_username.text.toString(),
                et_password.text.toString()
            )
        }

        btn_login.setOnClickListener {
            viewModel.login(
                et_username.text.toString(),
                et_password.text.toString()
            )
        }

        viewModel.login.observe(this, EventObserver {
            if (it.state == State.LOADING) {
                LoadingDialogFragment.show(
                    supportFragmentManager,
                    true,
                    DialogInterface.OnCancelListener {
                        viewModel.cancelLogin()
                    })
                return@EventObserver
            } else {
                LoadingDialogFragment.hide(supportFragmentManager)
            }
            if (it.state == State.SUCCESS) {
                // 登录成功
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                // 非Loading状态
                Toast.makeText(this, it.msg ?: getString(R.string.login_failed), Toast.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.loginFormState.observe(this, EventObserver { formState ->
            btn_login.isEnabled = formState.valid

            formState.userNameErr?.let {
                et_username.error = getString(it)
            }

            formState.passwordErr?.let {
                et_password.error = getString(it)
            }
        })
    }

    override fun finish() {
        HideKeyBoard.hideKeyBoard(this)
        super.finish()
    }
}