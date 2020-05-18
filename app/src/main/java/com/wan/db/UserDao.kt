package com.wan.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wan.data.user.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * 只获取唯一一条用户数据
     */
    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): User

    /**
     * 只获取唯一一条用户数据
     */
    @Query("SELECT * FROM user LIMIT 1")
    fun getUserLiveData(): LiveData<User>

    /**
     * 退出登录则清除表
     */
    @Query("DELETE FROM user")
    suspend fun clearAllUsers()
}