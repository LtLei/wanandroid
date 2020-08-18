package com.wan.core.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit

open class NetworkTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var service: TestService

    lateinit var mockWebServer: MockWebServer

    @ExperimentalSerializationApi
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val contentType = "application/json".toMediaType()
        val serializationConverterFactory = Json {
            ignoreUnknownKeys = true
        }.asConverterFactory(contentType)

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(serializationConverterFactory)
            .build()
            .create(TestService::class.java)
    }

    @After
    fun clearUp() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val bufferedSource = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(bufferedSource.readString(Charsets.UTF_8)))
    }
}