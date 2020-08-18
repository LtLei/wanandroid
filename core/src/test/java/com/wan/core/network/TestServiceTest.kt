package com.wan.core.network

import com.wan.core.Resource
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TestServiceTest : NetworkTest() {
    @Test
    fun getFakeUserSuccess() = runBlocking {
        enqueueResponse("get_user.json")

        val response: ApiResponse<FakeUser> = service.login("fake_username", "fake_password")
        assertTrue(response.isSuccess())
        assertEquals(response.data, FakeUser("LiHua", "male"))

        val resource =
            response.toResource { if (it.data == null) Resource.empty() else Resource.success(it.data) }
        assertEquals(resource, Resource.success(response.data))
    }

    @Test
    fun serverError() = runBlocking {
        enqueueResponse("get_user_server_error.json")

        val response: ApiResponse<FakeUser> = service.login("fake_username", "fake_password")
        assertTrue(!response.isSuccess())

        val resource =
            response.toResource { if (it.data == null) Resource.empty() else Resource.success(it.data) }
        assertEquals(resource, Resource.error<FakeUser>(-1000, "请求失败"))
    }

    @Test
    fun networkError() = runBlocking {
        val mockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(401)

        mockWebServer.enqueue(mockResponse)

        val resource = safeCall {
            val response: ApiResponse<FakeUser> = service.login("fake_username", "fake_password")
            response.toResource { if (it.data == null) Resource.empty() else Resource.success(it.data) }
        }
        assertEquals(resource, Resource.error<FakeUser>(401, "Client Error"))
    }
}