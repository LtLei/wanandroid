package com.wan.data.classify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wan.core.Resource
import com.wan.core.base.BaseRepository
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.suspendRunInTransaction
import com.wan.core.extensions.toResource
import com.wan.db.ClassifyDao
import com.wan.db.WanAndroidDb
import kotlinx.coroutines.coroutineScope

interface ClassifyRepository {
    val classifies: LiveData<Resource<List<ClassifyModel>>>

    suspend fun getClassifies(forceUpdate: Boolean = false)
}

class ClassifyRepositoryImpl(
    private val db: WanAndroidDb,
    private val classifyDao: ClassifyDao,
    private val classifyService: ClassifyService
) : BaseRepository(), ClassifyRepository {
    private val _classifies = MutableLiveData<Resource<List<ClassifyModel>>>()

    override val classifies: LiveData<Resource<List<ClassifyModel>>> = _classifies

    override suspend fun getClassifies(forceUpdate: Boolean) {
        _classifies.value = Resource.loading()
        if (forceUpdate) {
            // 直接网络请求
            fetchFromNetwork()
        } else {
            val dbResult = loadFromDb()
            if (dbResult.isNullOrEmpty()) {
                // 网络请求
                fetchFromNetwork()
            } else {
                _classifies.value = Resource.success(dbResult)
            }
        }
    }

    private suspend fun loadFromDb(): List<ClassifyModel> {
        return coroutineScope {
            val parentClassifies = classifyDao.getParentClassifies()
            parentClassifies.forEach {
                val childClassifies = classifyDao.getChildClassifies(it.classifyId)
                it.classifies = childClassifies
            }
            parentClassifies
        }
    }

    private suspend fun fetchFromNetwork() {
        val resource = safeCall {
            val classifies = classifyService.getClassifies()

            classifies.toResource {
                if (it.data.isNullOrEmpty()) {
                    Resource.empty()
                } else {
                    Resource.success(it.data)
                }
            }
        }

        resource.data?.let {
            saveLocal(it)
        }
        _classifies.value = resource
    }

    private suspend fun saveLocal(classifies: List<ClassifyModel>) {
        db.suspendRunInTransaction {
            classifyDao.insertModels(classifies)
            classifies.forEach {
                it.classifies?.run {
                    if (this.isNotEmpty()) {
                        classifyDao.insertModels(this)
                    }
                }
            }
        }
    }
}