package com.wan.data.articles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectStatus(
    val isCollect: Boolean,
    val position: Int
)

@Serializable
data class ArticlesResult(
    val refresh: Boolean,
    val hasMore: Boolean,
    val articles: MutableList<Article>? = null
)

@Serializable
data class ArticlesModel(
    var curPage: Int,
    var pageCount: Int,
    var over: Boolean,
    @SerialName("datas")
    val articles: MutableList<Article>? = null
) {
    override fun toString(): String {
        return "ArticlesModel(curPage=$curPage, pageCount=$pageCount, over=$over)"
    }
}

@Serializable
data class Article(
    val id: Int,
    val title: String,
    val desc: String? = null,
    val link: String,
    val chapterId: Int,
    val chapterName: String? = null,
    val superChapterId: Int = 0,
    val superChapterName: String? = null,
    val userId: Int,
    val author: String? = null,
    val niceDate: String? = null,
    val shareUser: String? = null,
    val niceShareDate: String? = null,
    var collect: Boolean = false,
    val fresh: Boolean = false,
    val zan: Int,
    val tags: List<Tag>? = null,

    /* 手动标记一篇文章是否为置顶 */
    var top: Boolean = false
)

@Serializable
data class Tag(
    val name: String,
    val url: String
)
