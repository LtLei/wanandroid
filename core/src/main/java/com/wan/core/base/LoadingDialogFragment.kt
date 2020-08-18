package com.wan.core.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wan.core.R


/**
 * 加载对话框.
 *
 * 使用 companion object functions 来控制显示与隐藏。
 *
 * 一点小优化：当Loading对话框显示时间很短时，UI效果就像闪了一下一样。因此这里做了优化：
 *  1. show之前有一定的delay，如果在delay之前就结束了，就不会再show出来；
 *  2. hide之前也有一定的delay，也就是说一旦show出来，至少显示一段时间。
 */
class LoadingDialogFragment : DialogFragment() {
    private var mStartTime: Long = -1L // 开始显示时的时间
    private var mPostedHide = false
    private var mPostedShow = false
    private var mDismissed = false

    private val mHandler: Handler = Handler()
    private var mDelayedShow: Runnable? = null
    private var mDelayedHide: Runnable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        removeCallbacks()
    }

    override fun onDetach() {
        super.onDetach()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallback(mDelayedShow)
        removeCallback(mDelayedHide)
    }

    private fun removeCallback(r: Runnable?) {
        r?.let { mHandler.removeCallbacks(it) }
    }

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

    override fun show(manager: FragmentManager, tag: String?) {
        // Reset the start time.
        mStartTime = -1
        mDismissed = false
        removeCallback(mDelayedHide)
        mPostedHide = false
        if (!mPostedShow) {
            mHandler.postDelayed(requireDelayedShow(manager, tag), MIN_DELAY)
            mPostedShow = true
        }
    }

    override fun dismiss() {
        mDismissed = true
        removeCallback(mDelayedShow)
        mPostedShow = false
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= MIN_SHOW_TIME || mStartTime == -1L) {
            // LoadingDialogFragment的显示时间已经超过了500ms或者还没有显示
            super.dismiss()
        } else {
            // LoadingDialogFragment的显示时间不足500ms
            if (!mPostedHide) {
                mHandler.postDelayed(requireDelayedHide(), MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    private fun requireDelayedShow(manager: FragmentManager, tag: String?): Runnable {
        return mDelayedShow ?: Runnable {
            mPostedShow = false
            if (!mDismissed) {
                mStartTime = System.currentTimeMillis()
                // 显示
                super.show(manager, tag)
            }
        }.also {
            mDelayedShow = it
        }
    }

    private fun requireDelayedHide(): Runnable {
        return mDelayedHide ?: Runnable {
            mPostedHide = false
            mStartTime = -1
            super.dismiss()
        }.also { mDelayedHide = it }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }

    companion object {
        private const val TAG = "com.wan.core.base.LoadingDialogFragment"
        private const val MIN_SHOW_TIME = 500L // ms
        private const val MIN_DELAY = 300L // ms

        // show方法存在 300 ms 的delay，如果在show之前调用了hide，因为FragmentManger get 不到值，需要手动清除delay任务。
        private var currentFragment: LoadingDialogFragment? = null

        private fun resetCurrentFragment() {
            currentFragment?.removeCallbacks()
            currentFragment = null
        }

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
            currentFragment = fragment
        }

        fun get(manager: FragmentManager): LoadingDialogFragment? {
            return manager.findFragmentByTag(TAG) as? LoadingDialogFragment
        }

        fun hide(manager: FragmentManager) {
            get(manager)?.dismiss()
            // 不被FragmentManager管理
            if (currentFragment != get(manager)) {
                currentFragment?.dismiss()
                // 清除delay
                resetCurrentFragment()
            }
        }
    }
}