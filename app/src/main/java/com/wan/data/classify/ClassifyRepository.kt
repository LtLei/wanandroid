package com.wan.data.classify

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.wan.core.Resource
import com.wan.core.base.BaseRepository
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource
import com.wan.db.ClassifyDao

interface ClassifyRepository {
    fun getClassifies(forceUpdate: Boolean): LiveData<Resource<List<ClassifyModel>>>
}

class DefaultClassifyRepository(
    private val classifyDao: ClassifyDao,
    private val classifyService: ClassifyService
) : BaseRepository(), ClassifyRepository {

    override fun getClassifies(forceUpdate: Boolean): LiveData<Resource<List<ClassifyModel>>> {
        return liveData {
            if (forceUpdate) {
                emit(fetchFromNetwork())
            } else {
                val dbResult = classifyDao.getClassifies()
                if (dbResult.isNotEmpty()) {
                    emit(Resource.success(dbResult))
                } else {
                    emit(fetchFromNetwork())
                }
            }
        }
    }

    private suspend fun fetchFromNetwork(): Resource<List<ClassifyModel>> {
        return safeCall {
            val classifies = classifyService.getClassifies()
            classifies.data?.let {
                classifyDao.insertClassifies(it)
            }
            classifies.toResource {
                if (it.data.isNullOrEmpty()) {
                    Resource.empty()
                } else {
                    Resource.success(it.data)
                }
            }
        }
    }
}