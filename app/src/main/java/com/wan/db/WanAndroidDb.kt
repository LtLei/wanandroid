package com.wan.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wan.data.classify.ClassifyModel
import com.wan.data.user.User

@Database(
    entities = [User::class, ClassifyModel::class],
    version = 1,
    exportSchema = true
)
abstract class WanAndroidDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun classifyDao(): ClassifyDao

    companion object {
        @Volatile
        private var instance: WanAndroidDb? = null

        fun getInstance(context: Context): WanAndroidDb {
            return instance ?: synchronized(WanAndroidDb::class) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WanAndroidDb::class.java,
                    "wanandroid.db"
                )
                    .build()
                    .also {
                        instance = it
                    }
            }
        }

        /* debug mode only. */
        fun destroyInstance() {
            synchronized(WanAndroidDb::class) {
                instance = null
            }
        }
    }
}