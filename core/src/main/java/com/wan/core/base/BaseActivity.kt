package com.wan.core.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * 即使什么都不做，也需要一个BaseActivity，总有一些配置需要每个页面响应，如果没有它就会丢失控制权。
 */
abstract class BaseActivity : AppCompatActivity() {
    fun displayHomeAsUp() {
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}