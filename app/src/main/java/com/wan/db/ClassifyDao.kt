package com.wan.db

import androidx.room.*
import com.wan.data.classify.ClassifyModel

@Dao
abstract class ClassifyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun inserts(models: List<ClassifyModel>)

    @Query("SELECT * FROM ClassifyModel WHERE parentChapterId = :pId")
    protected abstract fun getClassifiesByPId(pId: Int): List<ClassifyModel>

    @Transaction
    open suspend fun insertClassifies(classifies: List<ClassifyModel>) {
        inserts(classifies)
        classifies.forEach {
            it.classifies?.run {
                if (this.isNotEmpty()) {
                    inserts(this)
                }
            }
        }
    }

    @Transaction
    open suspend fun getClassifies(): List<ClassifyModel> {
        val parentClassifies = getClassifiesByPId(0)
        parentClassifies.forEach {
            val childClassifies = getClassifiesByPId(it.classifyId)
            it.classifies = childClassifies
        }
        return parentClassifies
    }
}