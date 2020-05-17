package com.wan.core.base

import android.content.DialogInterface

/**
 * 不同于[LoadingStateView]，这个是用于用户交互型显示加载中的对话框的
 *
 * [BaseView]会继承它，[BaseFragment]会实现它。
 */
interface LoadingDialogView {
    fun showDialog(cancellable: Boolean, onCancelListener: DialogInterface.OnCancelListener)

    fun hideDialog()
}