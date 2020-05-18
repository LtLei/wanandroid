package com.wan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wan.core.viewpager.BaseFragmentPagerAdapter
import com.wan.ui.classify.ClassifyFragment
import com.wan.ui.home.HomeFragment
import com.wan.ui.user.UserFragment

class MainFragmentPagerAdapter(fm: FragmentManager) :
    BaseFragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> ClassifyFragment()
            2 -> UserFragment()
            else -> throw IllegalStateException("unreachable code!")
        }
    }

    override fun getCount(): Int {
        return 3
    }
}