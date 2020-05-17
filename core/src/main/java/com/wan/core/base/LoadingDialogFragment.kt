package com.wan.core.base

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wan.core.R

/**
 * 加载对话框.
 *
 * 使用 companion object functions 来控制显示与隐藏。
 */
class LoadingDialogFragment : DialogFragment() {
    private var onCancelListener: DialogInterface.OnCancelListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loading_dialog_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog?.window
        window?.let {
            val params: WindowManager.LayoutParams = window.attributes
            params.gravity = Gravity.CENTER
            window.attributes = params
            window.setBackgroundDrawable(
                ColorDrawable(Color.WHITE)
            )
            //设置边距
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            window.setLayout((dm.widthPixels * 0.72).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }

    companion object {
        private const val TAG = "com.wan.core.base.LoadingDialogFragment"

        fun show(
            manager: FragmentManager,
            cancellable: Boolean = false,
            onCancelListener: DialogInterface.OnCancelListener? = null
        ) {
            val fragment = get(manager) ?: LoadingDialogFragment()
            fragment.onCancelListener = onCancelListener

            if (!fragment.isAdded) {
                fragment.isCancelable = cancellable
                fragment.show(manager, TAG)
            }
        }

        fun get(manager: FragmentManager): LoadingDialogFragment? {
            return manager.findFragmentByTag(TAG) as? LoadingDialogFragment
        }

        fun hide(manager: FragmentManager) {
            get(manager)?.dismiss()
        }
    }
}