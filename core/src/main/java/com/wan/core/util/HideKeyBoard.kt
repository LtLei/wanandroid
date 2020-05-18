package com.wan.core.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

//隐藏键盘
object HideKeyBoard {
    fun hideKeyBoard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        activity.currentFocus?.windowToken?.run {
            inputMethodManager?.hideSoftInputFromWindow(
                this,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}