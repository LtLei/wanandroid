package com.wan.data.user

import com.wan.core.network.ApiResponse
import retrofit2.http.Field
import retrofit2.http.POST

interface UserService {
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<Login>
}