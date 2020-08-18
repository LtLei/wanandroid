package com.wan.data.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wan.MainCoroutineRule
import com.wan.core.Resource
import com.wan.core.network.ApiResponse
import com.wan.core.network.MySerializableAny
import com.wan.db.UserDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class UserRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    private val userDao = mock(UserDao::class.java)
    private val userService = mock(UserService::class.java)
    private val user = User(100)
    private val userRepository = DefaultUserRepository(userDao, userService)

    @Test
    fun login() = runBlocking {
        val response = ApiResponse(data = user)
        `when`(userService.login(USER_NAME, USER_PWD)).thenReturn(response)
        val event = userRepository.login(USER_NAME, USER_PWD)

        verify(userService).login(USER_NAME, USER_PWD)
        verify(userDao).insert(user)
        verifyNoMoreInteractions(userService)
        verifyNoMoreInteractions(userDao)
        assertEquals(event.peekContent(), Resource.success(user))
    }

    @Test
    fun logout() = runBlocking {
        val any = MySerializableAny()
        // 即使返回error，最终的event也是success
        val response = ApiResponse(errorCode = 100, errorMsg = "请先登录", data = any)
        `when`(userService.logout()).thenReturn(response)
        val event = userRepository.logout()

        verify(userService).logout()
        verify(userDao).clearAllUsers()
        verifyNoMoreInteractions(userService)
        verifyNoMoreInteractions(userDao)
        assertEquals(event.peekContent(), Resource.success(any))
    }

    companion object {
        private const val USER_NAME = "user1"
        private const val USER_PWD = "123456"
    }
}