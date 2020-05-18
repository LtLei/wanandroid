package com.wan.data.user

import androidx.lifecycle.LiveData
import com.wan.bus.LogInOutBus
import com.wan.core.Resource
import com.wan.core.base.BaseRepository
import com.wan.core.event.Event
import com.wan.db.UserDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

interface UserRepository {
    /* 登录 */
    val login: LiveData<Event<Resource<User>>>

    val loginFormState: LiveData<Event<LoginFormState>>

    fun onFormChange(username: String, password: String)

    suspend fun login(username: String, password: String)

    /* 登出 */
    val logout: LiveData<Event<Resource<Nothing>>>

    suspend fun logout()

    /* 是否登录 */
    fun isLogin(): Boolean

    /* 单次获取当前登录的用户信息，适于单次操作 */
    fun getUser(): User?

    /* 获取持续更新的登录用户信息，适于页面数据渲染 */
    fun getUserLiveData(): LiveData<User>
}

class UserRepositoryImpl private constructor(
    private val userDao: UserDao,
    private val userService: UserService
) : BaseRepository(), UserRepository {
    private var _user: User? = null

    init {
        EventBus.getDefault().register(this)
        loadUser()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLogInOutEvent(logInOutBus: LogInOutBus) {
        Timber.d("onLogInOutEvent ----> isLogin : ${logInOutBus.login}")
        loadUser()
    }

    private fun loadUser() {
        GlobalScope.launch {
            _user = userDao.getUser()
        }
    }

    /* 负责登录相关行为 */
    private val loginDelegate by lazy {
        LoginDelegate(userDao, userService)
    }

    /* 负责登出相关行为 */
    private val logoutDelegate by lazy {
        LogoutDelegate(userDao, userService)
    }

    override val login: LiveData<Event<Resource<User>>>
        get() = loginDelegate.login

    override suspend fun login(username: String, password: String) {
        loginDelegate.login(username, password)
    }

    override val loginFormState: LiveData<Event<LoginFormState>>
        get() = loginDelegate.loginFormState

    override fun onFormChange(username: String, password: String) {
        loginDelegate.onFormChange(username, password)
    }

    override val logout: LiveData<Event<Resource<Nothing>>>
        get() = logoutDelegate.logout

    override suspend fun logout() {
        logoutDelegate.logout()
    }

    override fun isLogin(): Boolean {
        return getUser() != null
    }

    override fun getUser(): User? {
        return _user
    }

    override fun getUserLiveData(): LiveData<User> {
        return userDao.getUserLiveData()
    }

    companion object {
        @Volatile
        private var instance: UserRepositoryImpl? = null

        fun getInstance(userDao: UserDao, userService: UserService): UserRepositoryImpl {
            return instance ?: synchronized(UserRepositoryImpl::class.java) {
                instance ?: UserRepositoryImpl(userDao, userService).also {
                    instance = it
                }
            }
        }
    }
}