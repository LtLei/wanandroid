package com.wan.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.wan.R
import com.wan.core.base.BaseActivity
import com.wan.core.util.HideKeyBoard
import kotlinx.android.synthetic.main.search_activity.*

class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        btn_back.setOnClickListener { finish() }
        et_search.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        startSearch()
                    }
                }
                false
            }

            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    startSearch()
                    return@setOnKeyListener true
                }
                false
            }
        }

        btn_search.setOnClickListener { startSearch() }
    }

    private fun startSearch() {
        HideKeyBoard.hideKeyBoard(this)
        et_search.clearFocus()

        val keyword = et_search.text.toString()
        SearchFragment.startSearch(supportFragmentManager, R.id.fragment, keyword)
    }
}