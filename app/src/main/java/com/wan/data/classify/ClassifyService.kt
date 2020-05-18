package com.wan.data.classify

import com.wan.core.network.ApiResponse
import retrofit2.http.GET

interface ClassifyService {
    @GET("tree/json")
    suspend fun getClassifies(): ApiResponse<List<ClassifyModel>>
}