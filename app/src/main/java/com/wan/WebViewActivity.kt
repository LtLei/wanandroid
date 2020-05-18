package com.wan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.wan.core.base.BaseActivity
import kotlinx.android.synthetic.main.webview_activity.*


class WebViewActivity : BaseActivity() {
    private lateinit var mUrl: String
    private lateinit var mTitle: String

    private var mAgentWeb: AgentWeb? = null

    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            supportActionBar?.title = title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_activity)

        displayHomeAsUp()

        intent.let {
            mUrl = requireNotNull(it.getStringExtra(ARG_URL), { "arg url is null." })
            mTitle = it.getStringExtra(ARG_TITLE) ?: getString(R.string.default_webview_title)
        }

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(web_view_container, lp)
            .useDefaultIndicator(resources.getColor(R.color.colorAccent))
            .setWebChromeClient(mWebChromeClient)
            .createAgentWeb()
            .ready()
            .go(mUrl)
    }

    override fun onResume() {
        super.onResume()
        mAgentWeb?.webLifeCycle?.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (mAgentWeb?.handleKeyEvent(keyCode, event) == true) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    companion object {
        private const val ARG_URL = "arg_url"
        private const val ARG_TITLE = "arg_title"

        fun open(context: Context, url: String, title: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(ARG_URL, url)
                putExtra(ARG_TITLE, title)
            })
        }
    }
}