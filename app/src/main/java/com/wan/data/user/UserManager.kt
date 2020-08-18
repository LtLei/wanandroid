package com.wan.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.wan.BaseInjection
import com.wan.bus.LogInOutBus
import com.wan.db.DbInjection
import com.wan.db.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class UserManager private constructor(private val userDao: UserDao) {
    private var _user: User? = null

    fun isLogin(): Boolean {
        return _user != null
    }

    fun getUser(): User? = _user

    fun getUserLiveData(): LiveData<User> {
        return userDao.getUserLiveData()
            .distinctUntilChanged()
            .map {
                _user = it
                it
            }
    }

    init {
        EventBus.getDefault().register(this)
        loadUser()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLogInOutEvent(logInOutBus: LogInOutBus) {
        Timber.d("onLogInOutEvent ----> isLogin : ${logInOutBus.login}")
        if (!logInOutBus.login) {
            // 手动清除Cookie
            BaseInjection.provideRetrofitClient().clearCookie()
        }
        loadUser()
    }

    private fun loadUser() {
        GlobalScope.launch(Dispatchers.IO) {
            _user = userDao.getUser()
        }
    }

    companion object {
        @Volatile
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            return instance ?: synchronized(UserManager::class) {
                instance ?: UserManager(DbInjection.provideUserDao())
                    .also { instance = it }
            }
        }
    }
}