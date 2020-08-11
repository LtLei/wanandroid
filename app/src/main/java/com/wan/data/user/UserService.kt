package com.wan.data.user

import com.wan.core.network.ApiResponse
import com.wan.core.network.MySerializableAny
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<User>

    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<MySerializableAny>
}