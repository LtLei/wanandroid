package com.wan.ui.articles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wan.R
import com.wan.core.base.BaseActivity

class ArticlesActivity : BaseActivity() {

    private lateinit var type: String
    private lateinit var args: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articles_activity)

        displayHomeAsUp()

        type = requireNotNull(intent.getStringExtra(ARG_FRAGMENT_TYPE),
            { "ARG_FRAGMENT_TYPE is null." })
        args = requireNotNull(intent.getBundleExtra(ARG_FRAGMENT_ARGS),
            { "ARG_FRAGMENT_ARGS is null." })

        var fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment == null) {
            fragment = when (type) {
                TYPE_USER_COLLECT -> {
                    CollectArticlesFragment()
                }
                TYPE_AUTHOR -> {
                    AuthorArticlesFragment()
                }
                TYPE_CLASSIFY -> {
                    ClassifyArticlesFragment()
                }
                else -> {
                    // unreachable code.
                    require(false)
                    null
                }
            }

            fragment?.let {
                it.arguments = args
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment, it)
                    .commit()
            }
        }
    }

    companion object {
        const val TYPE_USER_COLLECT = "user_collect"
        const val TYPE_AUTHOR = "author"
        const val TYPE_CLASSIFY = "classify"

        private const val ARG_FRAGMENT_TYPE = "fragment_type"
        private const val ARG_FRAGMENT_ARGS = "arg_fragment_args"

        fun open(context: Context, type: String, bundle: Bundle) {
            Intent(context, ArticlesActivity::class.java).let {
                it.putExtra(ARG_FRAGMENT_TYPE, type)
                it.putExtra(ARG_FRAGMENT_ARGS, bundle)
            }.let {
                it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val newType = it.getStringExtra(ARG_FRAGMENT_TYPE)
            val newArgs = it.getBundleExtra(ARG_FRAGMENT_ARGS)
            if (newType == null || newArgs == null || areArgsTheSame(newType, newArgs)) {
                return@let
            }

            startActivity(Intent(this, ArticlesActivity::class.java).apply {
                putExtra(ARG_FRAGMENT_TYPE, newType)
                putExtra(ARG_FRAGMENT_ARGS, newArgs)
            })
        }
    }

    private fun areArgsTheSame(newType: String, newArgs: Bundle): Boolean {
        if (newType != this.type) return false
        return when (type) {
            TYPE_USER_COLLECT -> {
                CollectArticlesFragment.areArgsTheSame(args, newArgs)
            }
            TYPE_AUTHOR -> {
                AuthorArticlesFragment.areArgsTheSame(args, newArgs)
            }
            TYPE_CLASSIFY -> {
                ClassifyArticlesFragment.areArgsTheSame(args, newArgs)
            }
            else -> {
                require(false)
                false
            }
        }
    }
}