package com.wan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.wan.core.base.BaseActivity

class SplashActivity : BaseActivity() {
    private val handler: Handler by lazy { Handler() }
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.also {
            runnable = it
            handler.postDelayed(it, DELAY_TO_HOME)
        }
    }

    override fun onDestroy() {
        runnable?.let {
            handler.removeCallbacks(it)
        }
        super.onDestroy()
    }

    companion object {
        private const val DELAY_TO_HOME = 3_000L
    }
}