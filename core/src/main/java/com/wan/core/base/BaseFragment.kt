package com.wan.core.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.wan.core.view.loading.LoadingLayout

/**
 * 即使是非常希望不影响子类的实现，依然阻拦了[onCreateView]方法的重写，同时又提供了一个[retry]抽象方法要求子类实现。
 *
 * 我们希望这是唯一不合理的部分。
 *
 * 默认实现了 [LoadingStateView] 和 [LoadingDialogView]，你可以单纯用于方法调用，也可以用于 MVP 中的 V，就像 [BaseView] 那样。
 *
 * @param layoutId the layout id for [onCreateView]
 * @param withLoadingLayout 是否启用 [LoadingLayout]
 */
abstract class BaseFragment constructor(
    @LayoutRes private val layoutId: Int,
    private val withLoadingLayout: Boolean
) : Fragment(), LoadingStateView,
    LoadingDialogView {

    constructor(layoutId: Int) : this(layoutId, true)

    protected open fun retry() {}

    protected var mLoadingLayout: LoadingLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflatedView = inflater.inflate(layoutId, container, false)
        return if (withLoadingLayout) {
            mLoadingLayout = LoadingLayout(requireContext())
                .apply {
                    setContentView(inflatedView)
                    setRetryAction { retry() }
                }
            mLoadingLayout
        } else {
            inflatedView
        }
    }

    override fun onDestroyView() {
        mLoadingLayout = null
        super.onDestroyView()
    }

    override fun showLoading() {
        mLoadingLayout?.showLoading()
    }

    override fun showError() {
        mLoadingLayout?.showError()
    }

    override fun showEmpty() {
        mLoadingLayout?.showEmpty()
    }

    override fun showContent() {
        mLoadingLayout?.showContent()
    }

    override fun hasShownContent(): Boolean {
        return if (withLoadingLayout) {
            mLoadingLayout?.hasShownContent() ?: false
        } else true
    }

    override fun showDialog(
        cancellable: Boolean,
        onCancelListener: DialogInterface.OnCancelListener
    ) {
        LoadingDialogFragment.show(childFragmentManager, cancellable, onCancelListener)
    }

    override fun hideDialog() {
        LoadingDialogFragment.hide(childFragmentManager)
    }
}