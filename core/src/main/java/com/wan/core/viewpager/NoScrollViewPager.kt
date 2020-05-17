package com.wan.core.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager : ViewPager {
    private var noScroll = true

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(context: Context) : super(context)

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) false else super.onTouchEvent(arg0)
    }

    override fun setCurrentItem(item: Int) {
        if (noScroll) {
            setCurrentItem(item, false)
        } else {
            super.setCurrentItem(item)
        }
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (noScroll) false else super.onInterceptTouchEvent(arg0)
    }
}