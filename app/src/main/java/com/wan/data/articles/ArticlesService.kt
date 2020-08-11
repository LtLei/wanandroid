package com.wan.data.articles

import com.wan.core.network.ApiResponse
import com.wan.core.network.MySerializableAny
import retrofit2.http.*

/**
 * 所有和文章有关的操作，包括：
 * 1. 收藏与取消收藏
 * 2. 首页文章（置顶和分页两部分）
 * 3. 用户收藏的文章
 * 4. 用户创作的文章
 * 5. 根据关键字搜索的文章
 * 6. 根据分类搜索的文章
 */
interface ArticlesService {
    @POST("lg/collect/{articleId}/json")
    suspend fun collectArticle(
        @Path("articleId") articleId: Int
    ): ApiResponse<MySerializableAny>

    @POST("lg/uncollect_originId/{articleId}/json")
    suspend fun unCollectArticle(
        @Path("articleId") articleId: Int
    ): ApiResponse<MySerializableAny>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticlesByPage(
        @Path("page") page: Int
    ): ApiResponse<ArticlesModel>

    @GET("article/list/{page}/json")
    suspend fun getHomeArticlesByPage(
        @Path("page") page: Int
    ): ApiResponse<ArticlesModel>

    @GET("article/top/json")
    suspend fun getTopArticles(): ApiResponse<List<Article>>

    @GET("article/list/{page}/json")
    suspend fun getArticlesByClassify(
        @Path("page") page: Int,
        @Query("cid") secondaryClassifyId: Int
    ): ApiResponse<ArticlesModel>

    @GET("article/list/{page}/json")
    suspend fun getArticlesByAuthor(
        @Path("page") page: Int,
        @Query("author") author: String
    ): ApiResponse<ArticlesModel>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun getArticlesByKeyword(
        @Path("page") page: Int,
        @Field("k") keyword: String
    ): ApiResponse<ArticlesModel>
}