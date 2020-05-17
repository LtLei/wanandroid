package com.wan.core.view.loading

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.TextView
import com.wan.core.R

class LoadingLayout : FrameLayout {
    private var mRootView: FrameLayout? = null
    private var mLoadingView: View? = null
    private var mEmptyView: View? = null
    private var mTvEmpty: TextView? = null
    private var mErrorView: View? = null
    private var mTvError: TextView? = null
    private var mContentView: View? = null

    private var mRetryAction: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private fun init(context: Context) {
        mRootView =
            LayoutInflater.from(context).inflate(R.layout.loading_view, this) as FrameLayout
        mLoadingView = mRootView?.findViewById(R.id.view_loading)
    }

    fun setContentView(view: View) {
        if (mContentView == null) {
            mContentView = view
            val layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mRootView?.addView(mContentView, 0, layoutParams)
            hideContentView()
        }
    }

    fun setRetryAction(retryAction: () -> Unit) {
        mRetryAction = retryAction
    }

    fun showLoading() {
        assertMainThread()

        mLoadingView?.visibility = VISIBLE
        hideEmptyView()
        hideErrorView()
        hideContentView()
    }

    fun showError() {
        assertMainThread()

        val stub = mRootView?.findViewById<ViewStub>(R.id.stub_error)
        if (stub != null) {
            mErrorView = stub.inflate()
            mTvError = findViewById(R.id.tv_error)
            mTvError?.setOnClickListener { mRetryAction?.invoke() }
        } else {
            mErrorView?.visibility = VISIBLE
        }

        hideLoadingView()
        hideEmptyView()
        hideContentView()
    }

    fun showEmpty() {
        assertMainThread()

        val stub = mRootView?.findViewById<ViewStub>(R.id.stub_empty)
        if (stub != null) {
            mEmptyView = stub.inflate()
            mTvEmpty = findViewById(R.id.tv_empty)
            mTvEmpty?.setOnClickListener { mRetryAction?.invoke() }
        } else {
            mEmptyView?.visibility = VISIBLE
        }

        hideLoadingView()
        hideErrorView()
        hideContentView()
    }

    fun showContent() {
        assertMainThread()

        mContentView?.visibility = View.VISIBLE

        hideLoadingView()
        hideEmptyView()
        hideErrorView()
    }

    fun hasShownContent(): Boolean {
        return mContentView?.visibility == View.VISIBLE
    }

    private fun hideLoadingView() {
        mLoadingView?.run {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
            }
        }
    }

    private fun hideEmptyView() {
        mEmptyView?.run {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
            }
        }
    }

    private fun hideErrorView() {
        mErrorView?.run {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
            }
        }
    }

    private fun hideContentView() {
        mContentView?.run {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
            }
        }
    }

    private fun assertMainThread() {
        assert(Looper.myLooper() == Looper.getMainLooper())
    }
}
