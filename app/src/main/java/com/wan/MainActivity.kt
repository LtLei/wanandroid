package com.wan

import android.os.Bundle
import com.wan.core.base.BaseActivity
import com.wan.ui.home.HomeFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity() {
    private var fragmentPagerAdapter: MainFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        fragmentPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentPagerAdapter
        // Fragment View回收，但对象不回收。打开以下代码则View也不回收
        // viewPager.offscreenPageLimit = 2

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.main_classify -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.main_user -> {
                    viewPager.currentItem = 2
                    true
                }
                else -> false
            }
        }

        bottom_navigation.setOnNavigationItemReselectedListener {
            if (it.itemId == R.id.main_home) {
                // 首页
                (fragmentPagerAdapter?.getFragments()?.get(0) as? HomeFragment)?.runToTop()
            }
        }
    }

    override fun onDestroy() {
        fragmentPagerAdapter = null
        super.onDestroy()
    }
}
