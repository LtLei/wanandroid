package com.wan.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wan.data.classify.ClassifyModel

@Dao
interface ClassifyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<ClassifyModel>)

    @Query("SELECT * FROM ClassifyModel WHERE parentChapterId = 0")
    suspend fun getParentClassifies(): List<ClassifyModel>

    @Query("SELECT * FROM ClassifyModel WHERE parentChapterId = :pId")
    suspend fun getChildClassifies(pId: Int): List<ClassifyModel>
}