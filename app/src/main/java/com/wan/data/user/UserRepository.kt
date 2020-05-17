package com.wan.data.user

import com.wan.core.Resource
import com.wan.core.extensions.safeCall
import com.wan.core.extensions.toResource
import retrofit2.Response

interface UserRepository {
    suspend fun login(username:String, password:String):Resource<Login>
}

class UserRepositoryImpl(private val userService: UserService) : UserRepository {
    override suspend fun login(username: String, password: String): Resource<Login> {
        return safeCall {
            val response = userService.login(username, password)
            return response.toResource {
                if (response.data == null){
                    Resource.empty()
                }else{
                    Resource.success(response.data)
                }
            }
        }
    }
}