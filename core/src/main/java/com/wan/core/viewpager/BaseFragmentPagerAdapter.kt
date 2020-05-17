package com.wan.core.viewpager

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * the BaseFragmentPagerAdapter
 *
 * 使用 [getFragments] 方法获取 [Fragment]
 */
abstract class BaseFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val registeredFragments: SparseArray<Fragment> by lazy { SparseArray<Fragment>(5) }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getFragments(): SparseArray<Fragment> {
        return registeredFragments
    }
}