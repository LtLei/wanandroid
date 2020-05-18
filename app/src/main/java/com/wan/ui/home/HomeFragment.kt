package com.wan.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.wan.R
import com.wan.core.base.BaseFragment
import com.wan.ui.search.SearchActivity
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment(R.layout.home_fragment, false) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragment = childFragmentManager.findFragmentById(R.id.fragment_detail)
        if (fragment == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.fragment_detail, HomeDetailFragment())
                .commit()
        }

        fab.setOnClickListener {
            // todo 添加一个TODO
            Toast.makeText(context, "TODO系列敬请期待", Toast.LENGTH_SHORT).show()
        }

        tv_search.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    fun runToTop() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            (childFragmentManager.findFragmentById(R.id.fragment_detail) as? HomeDetailFragment)?.runToTop()
        }
    }
}