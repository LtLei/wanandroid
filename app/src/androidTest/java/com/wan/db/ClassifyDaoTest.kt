package com.wan.db

import com.wan.data.classify.ClassifyModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ClassifyDaoTest : DbTest() {
    @Test
    fun insertClassifies() = runBlocking {
        val classifyDao = db.classifyDao()

        classifyDao.insertClassifies(createClassifies())

        val classifies = classifyDao.getClassifies()
        assertThat(classifies.size, `is`(2))
        val classify1 = classifies.find { it.classifyId == 100 }
        assertThat(classify1, notNullValue())
        assertThat(classify1!!.classifies, notNullValue())
        assertThat(classify1.classifies!!.size, `is`(1))
    }

    private fun createClassifies(): List<ClassifyModel> {
        val classifies = arrayListOf<ClassifyModel>()
        val classify1 =
            ClassifyModel(classifyId = 100, name = "Classify1", courseId = 0, parentChapterId = 0)
        classify1.classifies = arrayListOf(
            ClassifyModel(
                classifyId = 101,
                name = "Classify1_Child1",
                courseId = 0,
                parentChapterId = 100
            )
        )
        val classify2 =
            ClassifyModel(classifyId = 200, name = "Classify2", courseId = 0, parentChapterId = 0)
        classifies.add(classify1)
        classifies.add(classify2)
        return classifies
    }
}