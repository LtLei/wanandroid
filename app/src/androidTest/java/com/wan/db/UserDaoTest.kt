package com.wan.db

import com.wan.data.user.User
import com.wan.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class UserDaoTest : DbTest() {
    @Test
    fun insertAndReplace() = runBlocking {
        val userDao = db.userDao()

        val user = createUser(uid = 100, nickname = "nick1")
        userDao.insert(user)

        val getFromDb = userDao.getUser()
        assertThat(getFromDb, notNullValue())
        assertThat(getFromDb!!.id, `is`(100))
        assertThat(getFromDb.nickname, `is`("nick1"))

        val replaceUser = createUser(uid = 100, nickname = "nick2")
        userDao.insert(replaceUser)

        val getReplaceUserFromDb = userDao.getUser()
        assertThat(getReplaceUserFromDb, notNullValue())
        assertThat(getReplaceUserFromDb!!.id, `is`(100))
        assertThat(getReplaceUserFromDb.nickname, `is`("nick2"))
    }

    @Test
    fun notifyLiveData() = runBlocking {
        val userDao = db.userDao()

        val user = createUser(uid = 100, nickname = "nick1")
        userDao.insert(user)

        val liveData = userDao.getUserLiveData()

        val user1 = liveData.getOrAwaitValue()
        assertEquals(user1, user)

        val replaceUser = createUser(uid = 100, nickname = "nick2")
        userDao.insert(replaceUser)

        val user2 = liveData.getOrAwaitValue()
        assertEquals(user2, replaceUser)
    }

    @Test
    fun clearAndGet() = runBlocking {
        println("clearAndGet: ${Thread.currentThread()}")

        val user = createUser(100, "nick1")
        db.userDao().insert(user)

        val get = db.userDao().getUser()
        assertThat(get, notNullValue())
        assertThat(get!!.nickname, `is`("nick1"))

        db.userDao().clearAllUsers()
        val clear = db.userDao().getUser()
        // clear 一定是null
        assertThat(clear, nullValue())
    }

    private fun createUser(uid: Int, nickname: String): User {
        return User(
            id = uid,
            nickname = nickname,
            icon = null,
            email = null,
            token = null,
            collectIds = listOf()
        )
    }
}