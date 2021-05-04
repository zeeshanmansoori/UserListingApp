package com.zee.userlistingapp.api

import com.zee.userlistingapp.BuildConfig
import com.zee.userlistingapp.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


const val BASE_URL = "https://api.github.com/"
interface GitUserSearchApi {
    @Headers(
        "Authorization: key=${BuildConfig.CLIENT_SECRET_KEY}"
    )
    @GET("search/users")
    suspend fun getUsers(@Query("q") q:String):Response<UserResponse>

}