package com.wan.data.articles

import com.google.gson.annotations.SerializedName

data class CollectStatus(
    val isCollect: Boolean,
    val position: Int
)

data class ArticlesResult(
    val refresh: Boolean,
    val hasMore: Boolean,
    val articles: MutableList<Article>?
)

data class ArticlesModel(
    var curPage: Int,
    var pageCount: Int,
    var over: Boolean,
    @SerializedName("datas")
    val articles: MutableList<Article>?
) {
    override fun toString(): String {
        return "ArticlesModel(curPage=$curPage, pageCount=$pageCount, over=$over)"
    }
}

data class Article(
    val id: Int,
    val title: String,
    val desc: String?,
    val link: String,
    val chapterId: Int,
    val chapterName: String?,
    val superChapterId: Int,
    val superChapterName: String?,
    val userId: Int,
    val author: String?,
    val niceDate: String?,
    val shareUser: String?,
    val niceShareDate: String?,
    var collect: Boolean,
    val fresh: Boolean,
    val zan: Int,
    val tags: List<Tag>?,

    /* 手动标记一篇文章是否为置顶 */
    var top: Boolean?
)

data class Tag(
    val name: String,
    val url: String
)
