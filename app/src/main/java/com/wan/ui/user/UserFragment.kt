package com.wan.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.wan.R
import com.wan.core.base.BaseFragment
import com.wan.data.user.UserManager
import com.wan.ui.articles.ArticlesActivity
import com.wan.ui.articles.AuthorArticlesFragment
import com.wan.ui.articles.CollectArticlesFragment
import com.wan.ui.login.LoginActivity
import com.wan.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.user_fragment.*

class UserFragment : BaseFragment(R.layout.user_fragment, false) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_settings.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
        }

        tv_articles.setOnClickListener {
            if (!UserManager.getInstance().isLogin()) {
                // login
                startActivityForResult(
                    Intent(context, LoginActivity::class.java),
                    REQUEST_CODE_MY_ARTICLES
                )
            } else {
                // open
                openMyArticlesActivity()
            }
        }

        tv_collect.setOnClickListener {
            if (!UserManager.getInstance().isLogin()) {
                // login
                startActivityForResult(
                    Intent(context, LoginActivity::class.java),
                    REQUEST_CODE_MY_COLLECT
                )
            } else {
                // open
                openMyCollectActivity()
            }
        }

        tv_todo.setOnClickListener {
            // todo TODO
            Toast.makeText(context, "TODO系列敬请期待", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launchWhenResumed {
            UserManager.getInstance().getUserLiveData().observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    // not login.
                    iv_avatar.setImageResource(R.mipmap.img_default_avatar)
                    tv_name.text = getString(R.string.not_login)
                    tv_coin.text = getString(R.string.login_to_see)
                    iv_avatar.setOnClickListener {
                        // login
                        // user 会自动更新，因此我们不需要 startActivityForResult
                        startActivity(Intent(context, LoginActivity::class.java))
                    }
                } else {
                    tv_name.text = it.nickname ?: getString(R.string.user_has_no_name)
                    Glide.with(this@UserFragment)
                        .load(it.icon)
                        .placeholder(R.mipmap.img_default_avatar)
                        .error(R.mipmap.img_default_avatar)
                        .into(iv_avatar)
                    // todo 积分获取
                    tv_coin.text = "敬请期待"

                    iv_avatar.setOnClickListener(null)
                }
            })
        }
    }

    private fun openMyArticlesActivity() {
        UserManager.getInstance().getUser()?.nickname?.let {
            ArticlesActivity.open(
                requireContext(),
                ArticlesActivity.TYPE_AUTHOR,
                AuthorArticlesFragment.args(it)
            )
        }
    }

    private fun openMyCollectActivity() {
        ArticlesActivity.open(
            requireContext(),
            ArticlesActivity.TYPE_USER_COLLECT,
            CollectArticlesFragment.args()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_MY_ARTICLES -> {
                    openMyArticlesActivity()
                }
                REQUEST_CODE_MY_COLLECT -> {
                    openMyCollectActivity()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_MY_ARTICLES = 100
        private const val REQUEST_CODE_MY_COLLECT = 101
    }
}