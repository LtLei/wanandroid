package com.wan.core.base

/**
 * 页面加载状态，页面初始化时会用到它
 *
 * [BaseView]会继承它，[BaseFragment]会实现它。
 */
interface LoadingStateView {
    /**
     * 显示正在加载
     */
    fun showLoading()

    /**
     * 显示加载失败
     */
    fun showError()

    /**
     * 显示未加载到内容
     */
    fun showEmpty()

    /**
     * 显示正常的UI
     */
    fun showContent()

    /**
     * 已经显示Content UI，加载过程完毕
     */
    fun hasShownContent(): Boolean
}