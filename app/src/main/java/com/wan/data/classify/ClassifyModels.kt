package com.wan.data.classify

import androidx.room.Entity
import androidx.room.Ignore
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ClassifyModel是一个嵌套结构。
 * parentChapterId = 0 说明是一级分类，其他则为二级分类。
 * 只有根据二级分类的 id 才可以查找文章
 */
@Entity(primaryKeys = ["classifyId"])
@Serializable
data class ClassifyModel(
    @SerialName("id")
    val classifyId: Int,
    val name: String,
    val courseId: Int,
    val parentChapterId: Int,
    @Ignore
    @SerialName("children")
    var classifies: List<ClassifyModel>?=null
) {
    /* for room only. */
    constructor(
        classifyId: Int,
        name: String,
        courseId: Int,
        parentChapterId: Int
    ) : this(classifyId, name, courseId, parentChapterId, null)
}

data class ClassifyFirstNode(
    val model: ClassifyModel,
    override val childNode: MutableList<BaseNode>? = mutableListOf<BaseNode>().apply {
        val second = model.classifies?.map {
            ClassifySecondNode(it)
        }
        second?.let {
            addAll(it)
        }
    }
) : BaseExpandNode()

data class ClassifySecondNode(
    val model: ClassifyModel,
    override val childNode: MutableList<BaseNode>? = null
) : BaseNode()